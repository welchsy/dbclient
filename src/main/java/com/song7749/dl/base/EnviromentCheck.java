package com.song7749.dl.base;

import static com.song7749.util.LogMessageFormatter.format;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Class Name : EnviromentCheck.java
 * Description : 환경 변수 체크
*
*  Modification Information
*  Modify Date 		Modifier	Comment
*  -----------------------------------------------
*  2015. 4. 28.		song7749	신규작성
*
* </pre>
*
* @author song7749
* @since 2015. 4. 28.
*/
public class EnviromentCheck {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 환경변수 DEV_ADOPTED_ENV를 확인하는 메서드
	 */
	public void checkHostEnviroment() {

		List<String> serviceableEnviromentList = new ArrayList<String>();
		serviceableEnviromentList.add("OPERATING_SERVER");
		serviceableEnviromentList.add("DEVELOPING_SERVER");
		serviceableEnviromentList.add("DEVELOPER_PC");

		if(System.getenv("DEV_ADOPTED_ENV")==null) {
			logger.info(
					format(
							"환경변수 DEV_ADOPTED_ENV 의 값이 설정되지 않았습니다.허용되는 환경변수 값 : ( {} ) 중 선택이 필요합니다.",
							"환경변수 확인"),
					serviceableEnviromentList.toString());
		}

		if(System.getenv("DEV_ADOPTED_ENV")!=null) {
			logger.info(
					format(
							"DEV_ADOPTED_ENV : {}",
							"환경변수 확인"),
					System.getenv("DEV_ADOPTED_ENV"));
		}

		if(System.getenv("DEV_ADOPTED_ENV") == null || ! serviceableEnviromentList.contains(System.getenv("DEV_ADOPTED_ENV"))) {
			logger.info(
					format(
							"DEV_ADOPTED_ENV가 바르게 설정되지 않았습니다.[현재값 : {}]",
							"환경변수 확인"),
					System.getenv("DEV_ADOPTED_ENV"));
		}
	}
}