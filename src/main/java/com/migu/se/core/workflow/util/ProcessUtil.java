package com.migu.se.core.workflow.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.impl.util.io.InputStreamSource;
import org.flowable.common.engine.impl.util.io.StreamSource;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;


/**
 * 流程工具类
 * @author haow
 *
 */
public class ProcessUtil {
	public static Collection<FlowElement> getProcesAllTaskCanvas(BpmnModel bpmnModel) {
		org.flowable.bpmn.model.Process process = bpmnModel.getMainProcess();
		if (null == process) {
			return null;
		}
		
	    Collection<FlowElement> flowElements = process.getFlowElements();
	    return flowElements;
	}
	
	/**
	 * 验证bpmnModel 是否是正确的bpmn xml文件
	 * @param bpmnModel bpmn对象
	 * @throws Exception 验证异常
	 */
	public static void validate(BpmnModel bpmnModel) throws Exception {
		//验证bpmnModel 是否是正确的bpmn xml文件
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		//验证失败信息的封装ValidationError
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
		if (validate.size() != 0) {
			throw new Exception("");
		}
	}
	
	/**
	 * xml文件转为BpmnModel对象
	 * @param bpmnPath 流程图路径
	 * @return BpmnModel 对象
	 * @throws Exception 异常
	 */
	public static BpmnModel readXMLFile(String bpmnPath) throws Exception {
	    InputStream xmlStream = ProcessUtil.class.getClassLoader().getResourceAsStream(bpmnPath);
	    StreamSource xmlSource = new InputStreamSource(xmlStream);
	    BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlSource, false, false, "UTF-8");
	    return bpmnModel;
	  }
}
