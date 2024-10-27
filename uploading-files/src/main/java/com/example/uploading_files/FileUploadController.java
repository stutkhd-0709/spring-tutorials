package com.example.uploading_files;

import com.example.uploading_files.storage.StorageFileNotFoundException;
import com.example.uploading_files.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadFiles(Model model) throws IOException {

        /*
         * storageService から現在アップロードされているファイルのパスを取得
         * そしてそのパスをタイムリーフ(Thymeleaf)にロードする
         * */
        model.addAttribute("files", storageService.loadAll().map(
                // from / of とかがあるときは、何かしらのインスタンスを引数の情報から作成してる
                // 今回の場合だと UriComponentsBuilder
                // UriComponentsBuilder は URIを作るためのbuilder
                // 今回だと FileUploadControllerのserveFileにアクセスするパスを生成する(/files/xxx.txt)
                path -> MvcUriComponentsBuilder.fromMethodName(
                        FileUploadController.class,
                        "serveFile",
                        path.getFileName().toString()
                ).build().toUri().toString()).collect(Collectors.toList())
        );

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        // Q. Resource型ってなんだろな
        Resource file = storageService.loadAsResource(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\""
                ).body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        storageService.store(file);
        /*
         * addFlashAttribute と addAttribute が存在する
         * 1. addAttribute は文字列型しか渡せない
         * 2. リダイレクトを受け取る側はaddFlashAttributeの場合は@ModelAttribute, addAttribute の場合は@RequestParameterを使う
         * 3. リロード時の挙動が違う, Flashの場合はflashMapを使うためリロードすると消える。
         * */
        redirectAttributes.addFlashAttribute("message",
                "File uploaded successfully! " + file.getOriginalFilename()
        );

        return "redirect:/";
    }

    // StorageFileNotFoundException というエラーが発生した時にこれが呼ばれる
    // この例外をハンドリングするのはこのControllerのscope内のみ
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
