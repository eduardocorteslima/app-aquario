package com.aquario;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class SlackService {

	static final Logger logger = Logger.getLogger(SlackService.class);

	@Value("${slack.url}")
	private String url;

	@Value("${slack.canal}")
	private String canal;
	

	public void gravarSlack(String msg, String usuario) {

		if (!StringUtils.isEmpty(msg)) {

			SlackApi slackApi = new SlackApi(url);

			SlackMessage slackMessage = new SlackMessage();
			slackMessage.setUsername(usuario);
			slackMessage.setIcon(":eye:");
			slackMessage.setChannel("#" + canal);
			slackMessage.setText(msg);

			slackMessage.prepare();
			slackApi.call(slackMessage);

		}
	}

}
