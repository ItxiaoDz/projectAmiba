package com.meidusa.amoeba.sqljep.function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;
import com.meidusa.amoeba.util.DbServerUtil;

/**
 * 
 * @author struct
 *
 */
public class GetDbServerById extends PostfixCommand {
	
	private String        poolName;
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	final public int getNumberOfParameters() {
		return 1;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}

	public static Comparable<?> getDbserverById(Comparable<?>  param) throws ParseException {
		if (param == null) {
			return null;
		}
		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs

		String url = "jdbc:mysql://127.0.0.1:3306/trade";

		// MySQL配置时的用户名

		String user = "root";

		// Java连接MySQL配置时的密码

		String password = "123456";
		String dbserver = null;
		String ipAddr =null;
		int port = 0;
		String dbUser = null;
		String dbPassword = null;
		String schema = null;
		String parent = null;
		try {

		// 加载驱动程序

		Class.forName(driver);

		// 连续数据库

		//DriverManager.setLoginTimeout(100);
		Connection conn = DriverManager.getConnection(url, user, password);
		if(!conn.isClosed())

		System.out.println("Succeeded connecting to the Database!");
		
		String sqlStr = "select * from user_dbserver where userId="+param;
		 Statement stmt = conn.createStatement() ;  
		 ResultSet rs = stmt.executeQuery(sqlStr) ; 
			while (rs.next()) {
				dbserver = rs.getString("dbserver");
				ipAddr = rs.getString("ipAddr");;
				port = rs.getInt("port");
				dbUser = rs.getString("dbUser");
				dbPassword = rs.getString("dbPassword");
				schema = rs.getString("dbPassword");
				parent = rs.getString("parent");
				System.out.println(dbserver);
			}
		
		conn.close();
		}catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
			} catch(SQLException e) {   
			e.printStackTrace();   
			} catch(Exception e) {   
			e.printStackTrace();   
			}
		
		if(DbServerUtil.isExists(dbserver)){
			return dbserver;
		}else{
			boolean flag = DbServerUtil.createDbServer(dbserver, ipAddr, port, dbUser, dbPassword, schema, parent);
			if(flag){
				return dbserver;
			}else {
				return null;
			}
		}
		
	}

	public Comparable<?> getResult(Comparable<?>... comparables)
			throws ParseException {
		return getDbserverById(comparables[0]);
	}
	
	
	/*public static void main(String[] args){
		String dd = "624265432";
		    int off = 0;
		    char val[] = dd.toCharArray();
		    int len = val.length;
		    int h = 0;
            for (int i = 0; i < len; i++) {
                h = 31*h + val[off++];
            }
		
		try {
			System.out.println(hash((dd)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}*/
}

