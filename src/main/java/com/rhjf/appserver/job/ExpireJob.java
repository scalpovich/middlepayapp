package com.rhjf.appserver.job;

import java.io.IOException;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.rhjf.appserver.authen.GetDynKey;

@Component("expireJobTask")
public class ExpireJob {

	@Scheduled(cron = "0 0 */2 * * ?")
	public void Job() throws IOException {
		System.out.println("do it");
		try {
			String msg = GetDynKey.getDynKey();
			if (msg.substring(0, 2).equals("00")) {
				System.out.println("秘钥[" + msg + "]");
			} else if (msg.substring(0, 2).equals("01")) {
				System.out.println(msg.substring(2));
			} else {
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("����ʱ��Ϊ" + new Date());
	}

}
