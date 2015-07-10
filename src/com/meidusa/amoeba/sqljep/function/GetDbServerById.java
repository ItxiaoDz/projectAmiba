package com.meidusa.amoeba.sqljep.function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.context.ProxyRuntimeContext;
import com.meidusa.amoeba.net.poolable.ObjectPool;
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
	private static Logger logger           = Logger.getLogger(GetDbServerById.class);
	private String        poolName;
	private String        sql;
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public void setSql(String sql) {
        this.sql = sql;
    }
	
	final public int getNumberOfParameters() {
		return 1;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}
	
	private Map<String, Object> query(Comparable<?>[] parameters) {
        ObjectPool pool = ProxyRuntimeContext.getInstance().getPoolMap().get(poolName);
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Map<String, Object> columnMap = null;
            conn = (Connection) pool.borrowObject();
            st = conn.prepareStatement(sql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i] instanceof Comparative) {
                        st.setObject(i + 1, ((Comparative) parameters[i]).getValue());
                    } else {
                        st.setObject(i + 1, parameters[i]);
                    }
                }
            }

            rs = st.executeQuery();
            if (rs.next()) {
                columnMap = new HashMap<String, Object>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = null;
                    String label = metaData.getColumnLabel(i);
                    if (label != null) {
                        columnName = label.toLowerCase();
                    } else {
                        columnName = metaData.getColumnName(i).toLowerCase();
                    }
                    Object columnValue = rs.getObject(i);
                    columnMap.put(columnName, columnValue);
                    if (logger.isDebugEnabled()) {
                        logger.debug("[columnName]:" + columnName + " [columnValue]:" + columnValue + " [args]:" + Arrays.toString(parameters));
                    }
                }
            } else {
                logger.error("no result!sql:[" + sql + "], args:" + Arrays.toString(parameters));
            }
            return columnMap;
        } catch (Exception e) {
            logger.error("execute sql error :" + sql, e);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e1) {
                }
            }

            if (conn != null) {
                try {
                    pool.returnObject(conn);
                } catch (Exception e) {
                }
            }
        }
    }
	
	public Comparable<?> getDbserverById(Comparable<?>  param) throws ParseException {
		if (param == null) {
			return null;
		}
		Comparable<?>[] params = new Comparable<?>[]{param};
		Map<String,Object> serverResult = query(params);
		
		String dbserver = (String) serverResult.get("dbserver");
		String ipAddr = (String) serverResult.get("ipaddr");
		int port = 0;
		if(serverResult.containsKey("port") && null != serverResult.get("port")){
			port = (Integer) serverResult.get("port");
		}
		String dbUser = (String) serverResult.get("dbUser");
		String dbPassword = (String) serverResult.get("dbPassword");
		String schema = (String) serverResult.get("schema");
		String parent = (String) serverResult.get("parent");
		
		/*String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs

		String url = "jdbc:mysql://127.0.0.1:3306/trade";

		// MySQL配置时的用户名

		String user = "root";

		// Java连接MySQL配置时的密码

		String password = "123456";
		
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
				schema = rs.getString("schema");
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
			}*/
		
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

