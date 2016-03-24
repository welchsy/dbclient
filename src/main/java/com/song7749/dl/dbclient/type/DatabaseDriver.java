package com.song7749.dl.dbclient.type;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.song7749.dl.dbclient.entities.ServerInfo;
import com.song7749.util.StringUtils;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 * Class Name : DatabaseDriver.java
 * Description : 드라이버 설정, 추가 DBMS 지원을 위해서는 해당 ENUM 에 추가해야 한다.
 *
 *
 *  Modification Information
 *  Modify Date 		Modifier	Comment
 * -----------------------------------------------
 *  2014. 4. 28.		song7749	신규작성
 *
 * </pre>
 *
 * @author song7749
 * @since 2014. 4. 28.
 *
 *
 *
 */
@ApiModel
public enum DatabaseDriver {

	@ApiModelProperty
	mysql(
			// dbms
			"mysql",
			// driverName
			"com.mysql.jdbc.Driver",
			// connect url
			"jdbc:mysql://{host}:{port}/{schemaName}?autoReconnect=true&useUnicode=true&createDatabaseIfNotExist=true&characterEncoding={charset}",
			// table list
			"SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA='{schemaName}' AND TABLE_TYPE='BASE TABLE'",
			// field list
			"SELECT ORDINAL_POSITION COLUMN_ID,COLUMN_NAME,IS_NULLABLE NULLABLE,COLUMN_KEY,DATA_TYPE,COLUMN_TYPE DATA_LENGTH,CHARACTER_SET_NAME CHARACTER_SET,EXTRA,COLUMN_DEFAULT DEFAULT_VALUE,COLUMN_COMMENT COMMENTS FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='{schemaName}' AND TABLE_NAME='{tableName}'",
			// index list
			"SELECT TABLE_NAME OWNER, INDEX_NAME, INDEX_TYPE, if(NON_UNIQUE=0,'UNIQUE','NOT_UNIQUE') as UNIQUENESS, CARDINALITY, COLUMN_NAME, SEQ_IN_INDEX COLUMN_POSITION, 'ASC' as DESCEND FROM information_schema.statistics WHERE table_name='{tableName}' AND TABLE_SCHEMA='{schemaName}'",
			// explain
			null,
			// view list
			"SELECT TABLE_NAME AS VIEW_NAME,VIEW_DEFINITION AS TEXT FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA='{schemaName}'",
			// view detail
			"",
			// view source
			"",
			// procedure list
			"SELECT SPECIFIC_NAME as NAME, LAST_ALTERED as LAST_UPDATE FROM INFORMATION_SCHEMA.ROUTINES  WHERE ROUTINE_TYPE='PROCEDURE' AND ROUTINE_SCHEMA='{schemaName}'",
			// procedure detail
			"",
			// procedure source
			"SELECT ROUTINE_DEFINITION as TEXT FROM INFORMATION_SCHEMA.ROUTINES  WHERE ROUTINE_TYPE='PROCEDURE' AND ROUTINE_SCHEMA='{schemaName}' AND SPECIFIC_NAME='{name}'",
			// function list
			"SELECT SPECIFIC_NAME as NAME, LAST_ALTERED as LAST_UPDATE FROM INFORMATION_SCHEMA.ROUTINES  WHERE ROUTINE_TYPE='FUNCTION' AND ROUTINE_SCHEMA='{schemaName}'",
			// function detail
			"",
			// function source
			"SELECT ROUTINE_DEFINITION as TEXT FROM INFORMATION_SCHEMA.ROUTINES  WHERE ROUTINE_TYPE='FUNCTION' AND ROUTINE_SCHEMA='{schemaName}' AND SPECIFIC_NAME='{name}'",
			// sequence list
			"SELECT concat(c.table_name, '.', c.column_name) as NAME, t.AUTO_INCREMENT as LAST_VALUE, '1' as MIN_VALUE, COLUMN_TYPE as MAX_VALUE, '1' as INCREMENT_BY from information_schema.columns c join information_schema.tables t on(c.table_schema=t.table_schema and c.table_name=t.table_name) where c.table_schema='{schemaName}' and c.extra='auto_increment'",
			// sequence detail
			"",
			// process list
			"SELECT ID, info as SQL_TEXT FROM information_schema.processlist",
			// kill connection
			"KILL CONNECTION {id}"),

	@ApiModelProperty
	oracle(
			// dbms
			"oracle",
			// driverName
			"oracle.jdbc.driver.OracleDriver",
			// url
			"jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST={host})(PORT={port})))(CONNECT_DATA=(SERVICE_NAME={schemaName})))",
			// table list
			"SELECT T1.TABLE_NAME TABLE_NAME,T2.COMMENTS TABLE_COMMENT FROM USER_TABLES T1, USER_TAB_COMMENTS T2 WHERE T2.TABLE_NAME(+) = T1.TABLE_NAME",
			// field list
			"SELECT a.COLUMN_ID,a.COLUMN_NAME,a.NULLABLE,decode(b.CONSTRAINT_TYPE,null,'NO','YES') COLUMN_KEY,a.DATA_TYPE,a.DATA_LENGTH,'' CHARACTER_SET,a.DATA_SCALE EXTRA,a.DATA_DEFAULT DEFAULT_VALUE,c.COMMENTS COMMENTS FROM USER_TAB_COLUMNS a, ( SELECT a.TABLE_NAME,a.COLUMN_NAME,b.CONSTRAINT_TYPE FROM USER_CONS_COLUMNS a, USER_CONSTRAINTS b WHERE a.TABLE_NAME = b.TABLE_NAME AND a.CONSTRAINT_NAME = b.CONSTRAINT_NAME AND b.CONSTRAINT_TYPE='P') b, USER_COL_COMMENTS c  WHERE a.TABLE_NAME = b.TABLE_NAME (+) AND a.COLUMN_NAME = b.COLUMN_NAME (+) AND a.TABLE_NAME = c.TABLE_NAME (+) AND a.COLUMN_NAME = c.COLUMN_NAME (+) AND a.TABLE_NAME = '{tableName}' ORDER BY a.COLUMN_ID",
			// index list
			"SELECT a.OWNER, a.INDEX_NAME, a.INDEX_TYPE, a.UNIQUENESS, a.NUM_ROWS CARDINALITY , b.COLUMN_NAME, b.COLUMN_POSITION,b.DESCEND FROM ALL_INDEXES a, ALL_IND_COLUMNS b WHERE a.index_name = b.index_name  AND a.table_name='{tableName}'",
			// explain
			"SELECT * from table(dbms_xplan.display('plan_table',null,'typical',null))",
			// view list
			"SELECT VIEW_NAME,TEXT FROM USER_VIEWS",
			// view detail
			"",
			// view source
			"",
			// procedure list
			"SELECT OBJECT_NAME as NAME, LAST_DDL_TIME as LAST_UPDATE from user_objects uo where uo.object_type = 'PROCEDURE'",
			// procedure detail
			"",
			// procedure source
			"SELECT SUBSTR(XMLAgg(XMLElement(x, '$^$', us.text) ORDER BY us.line).Extract('//text()').getClobVal(), 2) as text from user_source us where name = upper('{name}') GROUP BY us.name",
			// function list
			"SELECT OBJECT_NAME as NAME, LAST_DDL_TIME as LAST_UPDATE from user_objects uo where uo.object_type = 'FUNCTION'",
			// function detail
			"",
			// function source
			"SELECT SUBSTR(XMLAgg(XMLElement(x, '$^$', us.text) ORDER BY us.line).Extract('//text()').getClobVal(), 2) as text from user_source us where name = upper('{name}') GROUP BY us.name",
			// sequence list
			"SELECT SEQUENCE_NAME as NAME,LAST_NUMBER as LAST_VALUE, MIN_VALUE, MAX_VALUE, INCREMENT_BY from user_sequences",
			// sequence source
			"",
			// process list
			"SELECT concat(concat(s.sid , ','), s.serial#) as ID, sql.sql_text as SQL_TEXT from v$session s join v$sql sql on s.sql_id = sql.sql_id where s.program='dbClient'",
			// kill process
			"alter system kill session '{id}'");

	Logger logger = LoggerFactory.getLogger(getClass());

	private String dbms;
	private String driverName;
	private String url;
	private String tableListQuery;
	private String fieldListQuery;
	private String indexListQuery;
	private String explainQuery;
	private String viewListQuery;
	private String viewDetailQuery;
	private String viewSourceQuery;
	private String procedureListQuery;
	private String procedureDetailQuery;
	private String procedureSourceQuery;
	private String functionListQuery;
	private String functionDetailQuery;
	private String functionSourceQuery;
	private String sequenceListQuery;
	private String sequenceDetailQuery;
	private String processListQuery;
	private String killProcessQuery;

	DatabaseDriver(
		String dbms,
		String driverName,
		String url,
		String tableListQuery,
		String fieldListQuery,
		String indexListQuery,
		String explainQuery,
		String viewListQuery,
		String viewDetailQuery,
		String viewSourceQuery,
		String procedureListQuery,
		String procedureDetailQuery,
		String procedureSourceQuery,
		String functionListQuery,
		String functionDetailQuery,
		String functionSourceQuery,
		String sequenceListQuery,
		String sequenceDetailQuery,
		String processListQuery,
		String killProcessQuery) {

		this.dbms					= dbms;
		this.driverName				= driverName;
		this.url					= url;
		this.tableListQuery			= tableListQuery;
		this.fieldListQuery			= fieldListQuery;
		this.indexListQuery			= indexListQuery;
		this.explainQuery			= explainQuery;
		this.viewListQuery			= viewListQuery;
		this.viewDetailQuery		= viewDetailQuery;
		this.viewSourceQuery		= viewSourceQuery;
		this.procedureListQuery		= procedureListQuery;
		this.procedureDetailQuery 	= procedureDetailQuery;
		this.procedureSourceQuery 	= procedureSourceQuery;
		this.functionListQuery		= functionListQuery;
		this.functionDetailQuery	= functionDetailQuery;
		this.functionSourceQuery	= functionSourceQuery;
		this.sequenceListQuery		= sequenceListQuery;
		this.sequenceDetailQuery	= sequenceDetailQuery;
		this.processListQuery		= processListQuery;
		this.killProcessQuery		= killProcessQuery;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getUrl(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getTableListQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, tableListQuery);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getCause());
		}

	}

	public String getFieldListQueryQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, fieldListQuery);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getCause());
		}

	}

	public String getIndexListQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, indexListQuery);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getCause());
		}

	}

	public String getViewListQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, viewListQuery);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getProcedureListQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, procedureListQuery);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getProcedureDetailQuery(ServerInfo serverInfo,String name){
		try{
			String detailReplace = StringUtils.replace(procedureDetailQuery,"$^$","\n");
			return StringUtils.replacePatten("\\{name\\}",name, repalceServerInfo(serverInfo, detailReplace));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getFunctionListQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, functionListQuery);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getFunctionDetailQuery(ServerInfo serverInfo,String name){
		try{
			String detailReplace = StringUtils.replace(functionDetailQuery,"$^$","\n");
			return StringUtils.replacePatten("\\{name\\}",name, repalceServerInfo(serverInfo, detailReplace));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getSequenceListQueryQuery(ServerInfo serverInfo) {
		try {
			return repalceServerInfo(serverInfo, sequenceListQuery);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getCause());
		}
	}

	public String getProcessListQuery(){
		return processListQuery;
	}

	public String getProcessKillQuery(String id){
		return StringUtils.replacePatten("\\{id\\}",id, killProcessQuery);
	}

	public String getDbms() {
		return dbms;
	}

	public String getExplainQuery() {
		return explainQuery;
	}

	private String repalceServerInfo(ServerInfo serverInfo, String str)
			throws IllegalArgumentException, IllegalAccessException {

		for (Field f : serverInfo.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if(null!=f.get(serverInfo)){
				str = StringUtils.replacePatten("\\{" + f.getName() + "\\}",f.get(serverInfo).toString(), str);
			}
		}
		return str;
	}
}