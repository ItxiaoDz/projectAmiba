package com.meidusa.amoeba.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


import com.alibaba.china.jdbc.common.CharsetParameter;
import com.alibaba.china.jdbc.factory.ProxyFactory;

/**
 * <pre>
 * ���Oracle JDBC Driver��wrapper����Ҫ�������ת��������
 * ��������:Class.forName(&quot;com.alibaba.china.jdbc.SimpleDriver&quot;) 
 * URL���Ӹ�ʽ:jdbc:oracle:thin:@10.0.65.55:1521:ocndb
 * </pre>
 * 
 * @author hexianmao 2007 ���� 6 11:38:25
 */
public class DriverWrapper implements Driver {

    private ProxyFactory factory = new com.alibaba.china.jdbc.factory.SimpleProxyFactory();

    private Driver driver;

    private String clientEncoding;

	private String serverEncoding;
	
    public String getClientEncoding() {
		return clientEncoding;
	}


	public void setClientEncoding(String clientEncoding) {
		this.clientEncoding = clientEncoding;
	}


	public String getServerEncoding() {
		return serverEncoding;
	}


	public void setServerEncoding(String serverEncoding) {
		this.serverEncoding = serverEncoding;
	}
	
    public ProxyFactory getFactory() {
		return factory;
	}


	public Driver getDriver() {
		return driver;
	}


	public void setFactory(ProxyFactory factory) {
		this.factory = factory;
	}


	public void setDriver(Driver driver) {
		this.driver = driver;
	}


	public DriverWrapper(){
    }


    /**
     * <pre>
     * ����������Ƿ���Ϊ����Դ򿪵��� URL �����ӡ�
     * ע��÷�����#DriverManager.getDriver(String)�б����ã�����ȡ����Ӧ��driver��
     * </pre>
     */
    public boolean acceptsURL(String url) throws SQLException {
        return driver.acceptsURL(url);
    }

    /**
     * ��ͼ����һ������ URL ����ݿ����ӡ�
     */
    public Connection connect(String url, Properties info) throws SQLException {
        Properties p = new Properties();
        p.putAll(info);
        Connection conn = driver.connect(url, p);

        CharsetParameter param = new CharsetParameter();
        param.setClientEncoding(this.getClientEncoding());
        param.setServerEncoding(this.getServerEncoding());
        return factory.getConnection(param, conn);
    }

    /**
     * ���������������汾�š�
     */
    public int getMajorVersion() {
        return driver.getMajorVersion();
    }

    /**
     * ��ô������Ĵΰ汾�š�
     */
    public int getMinorVersion() {
        return driver.getMajorVersion();
    }

    /**
     * ��ô������Ŀ���������Ϣ��
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driver.getPropertyInfo(url, info);
    }

    /**
     * �����������Ƿ���һ������� JDBC CompliantTM �����
     */
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }


	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
