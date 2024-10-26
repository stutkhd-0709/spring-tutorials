package com.example.scheduling_tasks;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ScheduledTasksTest {

	/*
	* 特定のBeanをテスト中に呼び出し、テストの動作が正しく行われたか確認する
	* */
	@SpyBean
	ScheduledTasks tasks;

	@Test
	public void reportCurrentTime() {
		/*
		* サーバー起動時に10秒待つ
		* verifyの引数にSpyBeanを渡し、そのBeanのメソッドが呼ばれたか確認してる
		* */
		await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
			verify(tasks, atLeast(2)).reportCurrentTime();
		});
	}
}
