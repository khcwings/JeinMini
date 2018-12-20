package com.jein.mini.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.jein.mini.biz.common.domain.CommonHistoryMail;
import com.jein.mini.biz.common.persistence.CommonHistoryMailRepository;
import com.jein.mini.biz.common.vo.ResultVO;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.constant.CommonMessageConstrant;
import com.jein.mini.util.DateTimeUtil;

@Service
public class MailService {
	private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private CommonHistoryMailRepository commonHistoryMailRepo;

	/**
	 * 본문 내용이 TEXT로 된 메일을 발송한다. 
	 * @param receiver
	 * @param subject
	 * @param content
	 * @return
	 */
	public ResultVO sendTextMail(String receiver, String subject, String content) {
		List<String> receiverList = new ArrayList<String>();
		receiverList.add(receiver);
		return sendTextMail(receiverList, subject, content, null);
	}
	
	/**
	 * 본문 내용이 TEXT로 된 메일을 발송한다.  
	 * @param receiverList
	 * @param subject
	 * @param content
	 * @return
	 */
	public ResultVO sendTextMail(List<String> receiverList, String subject, String content) {
		return sendTextMail(receiverList, subject, content, null);
	}
	
	
	/**
	 * 본문 내용이 TEXT로 된 메일을 발송한다.  
	 * @param receiverList
	 * @param subject
	 * @param content
	 * @param attachFiles
	 * @return
	 */
	public ResultVO sendTextMail(List<String> receiverList, String subject, String content, List<String> attachFiles) {
		LOG.debug("# [Mail Service]Send Text Mail Start");
		ResultVO retVO 		= new ResultVO();
		String attachYn     = CommonConstant.VALUE_NO;
		String tempReceiver = CommonConstant.VALUE_EMPTY;
		if(receiverList == null || receiverList.isEmpty()) {
			retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_RECEIVER);
			saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_RECEIVER);
		} else if(subject == null || subject.isEmpty()) {
			retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_SUBJECT);
			saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_SUBJECT);
		} else {
			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

				if(!(attachFiles == null || attachFiles.isEmpty())) {
					for(String filePath : attachFiles) {
						if(!(filePath == null || filePath.isEmpty())) {
							File file = new File(filePath);
							helper.addAttachment(file.getName(), file);
							attachYn = CommonConstant.VALUE_YES;
						}
					}
				}
				
				helper.setText(content, false);
				helper.setSubject(subject);

				for(String receiver :receiverList) {
					tempReceiver = receiver;
					helper.setTo(receiver);
					mailSender.send(message);
					saveHistory("", receiver, subject, attachYn, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.MAIL_SUCCESS_MSG);
				}	
			} catch(MailException e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Text Mail ERROR[MailException] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			} catch(MessagingException e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Text Mail ERROR[MessagingException] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			} catch(Exception e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Text Mail ERROR[Exception] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			}	
		}

		retVO.setStatus(CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.MAIL_SUCCESS_MSG);
		LOG.debug("# [Mail Service]Send Text Mail End => Result : " + retVO.toString());
		return retVO;
	}

	/**
	 * 본문 내용이 HTML로 된 메일을 발송한다. 
	 * @param templatePath
	 * @param receiver
	 * @param subject
	 * @param model
	 * @return
	 */
	public ResultVO sendTemplateMail(String templatePath, String receiver, String subject, Map<String, Object> model) {
		List<String> receiverList = new ArrayList<String>();
		receiverList.add(receiver);
		return sendTemplateMail(templatePath, receiverList, subject, model, null);
	}

	/**
	 * 본문 내용이 HTML로 된 메일을 발송한다. 
	 * @param templatePath
	 * @param receiverList
	 * @param subject
	 * @param model
	 * @return
	 */
	public ResultVO sendTemplateMail(String templatePath, List<String> receiverList, String subject, Map<String, Object> model) {
		return sendTemplateMail(templatePath, receiverList, subject, model, null);
	}

	/**
	 * 본문 내용이 HTML로 된 메일을 발송한다. 
	 * @param templatePath
	 * @param receiverList
	 * @param subject
	 * @param model
	 * @param attachFiles
	 * @return
	 */
	public ResultVO sendTemplateMail(String templatePath, List<String> receiverList, String subject, Map<String, Object> model, List<String> attachFiles) {
		LOG.debug("# [Mail Service]Send Template Mail Start");
		ResultVO retVO 		= new ResultVO();
		String attachYn     = CommonConstant.VALUE_NO;
		String tempReceiver = CommonConstant.VALUE_EMPTY;
		if(templatePath == null || templatePath.isEmpty()) {
			retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_TEMPLATE_PATH);
			saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_TEMPLATE_PATH);
		} else if(receiverList == null || receiverList.isEmpty()) {
			retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_RECEIVER);
			saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_RECEIVER);
		} else if(subject == null || subject.isEmpty()) {
			retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_SUBJECT);
			saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_NO_SUBJECT);
		} else {
			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

				if(!(attachFiles == null || attachFiles.isEmpty())) {
					for(String filePath : attachFiles) {
						if(!(filePath == null || filePath.isEmpty())) {
							File file = new File(filePath);
							helper.addAttachment(file.getName(), file);
							attachYn = CommonConstant.VALUE_YES;
						}
					}
				}			        			       

				helper.setText(getProcessedHtml(model, templatePath), true);
				helper.setSubject(subject);
				
				for(String receiver :receiverList) {
					tempReceiver = receiver;
					helper.setTo(receiver);
					mailSender.send(message);
					saveHistory("", receiver, subject, attachYn, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.MAIL_SUCCESS_MSG);
				}			        
			} catch(MailException e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Template Mail ERROR[MailException] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			} catch(MessagingException e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Template Mail ERROR[MessagingException] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			} catch(Exception e) {
				e.printStackTrace();
				LOG.error("# [Mail Service]Send Template Mail ERROR[Exception] => " + e.getMessage());
				retVO.setStatus(CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
				saveHistory("", tempReceiver, subject, attachYn, CommonConstant.RESULT_ERROR, CommonMessageConstrant.MAIL_ERROR_UNKNOWN_EXCEPTION);
			}	
		}

		retVO.setStatus(CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.MAIL_SUCCESS_MSG);
		LOG.debug("# [Mail Service]Send Template Mail End => Result : " + retVO.toString());
		return retVO;
	}

	/**
	 * 템플릿 파일을 읽어 전달받은 데이터를 세팅하고 html을 리턴한다. 
	 * @param model
	 * @param templateName
	 * @return
	 */
	private String getProcessedHtml(Map<String, Object> model,String templateName) {
		Context context = new Context();
		if (model != null) {
			model.forEach((key,value) -> context.setVariable(key, value));
			return templateEngine.process(templateName, context);
		}
		return "";
	}
	
	private void saveHistory(String userId, String receiver, String subject, String attachYn, String resultCode, String resultMsg) {
		try {
			CommonHistoryMail history = new CommonHistoryMail();
			history.setReqTime(DateTimeUtil.getCurrentDateTimeSSS());
			history.setUserId(userId);
			history.setSubject(subject);
			history.setReceiver(receiver);
			history.setAttachYn(attachYn.charAt(0));		
			history.setResultCode(resultCode.charAt(0));
			history.setResultMsg(resultMsg);
			commonHistoryMailRepo.save(history);
		} catch (Exception e) {
			LOG.error("# [Mail Service]SAVE HISTORY ERROR : " + e.getMessage());
		}		
	}
}
