package kr.co.reyonpharm.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class RawDataImageView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sabun = (String) model.get("sabun");

		// Database credentials
		String url = EncriptBasicDataSource.decriptString(Constants.configProp.getProperty(Constants.RYHR_JDBC_URL));
		String username = EncriptBasicDataSource.decriptString(Constants.configProp.getProperty(Constants.RYHR_JDBC_USERNAME));
		String password = EncriptBasicDataSource.decriptString(Constants.configProp.getProperty(Constants.RYHR_JDBC_PASSWORD));

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(url, username, password);

			String queStr = "SELECT PHOTO FROM RYHR.RYHAA02MT WHERE SABUN = '" + sabun + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(queStr);
			rs.next();

			InputStream in = rs.getBinaryStream(1);

			response.setContentType("image/jpeg");
			response.setHeader("Content-Disposition", "filename=\"user.jpg\"");
			response.setHeader("Content-Transfer-Encoding", "binary");

			byte[] bytes = new byte[1024];
			OutputStream os = response.getOutputStream();

			while (true) {
				int red = in.read(bytes, 0, bytes.length);
				if (red < 0) {
					break;
				}
				os.write(bytes, 0, red);
			}

			os.flush();
			os.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
