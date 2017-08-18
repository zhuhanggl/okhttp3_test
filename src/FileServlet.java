

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/FileServlet")
@MultipartConfig
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Post请求成功");
		response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
		Part part= request.getPart("t");
        if(part==null) {
        	System.out.println("part上传失败");
        }else {
        	System.out.println("part上传成功前");
        	String header = part.getHeader("content-disposition");
            System.out.println(header);
            String fileName = getFileName(header);
        	String savePath = "F:/Program Files/httpd-2.4.27-x64-vc14/"
        			+ "Apache24/htdocs/hang/upload";
            part.write(savePath + File.separator + fileName);//目测这句话是
            //耗时的，这是真正意义上的上传
        	System.out.println("part上传成功后");
        	response.getWriter().append("q");
        }
	}
	
	public String getFileName(String header) {
        /**
         * header 为 form-data; name="file"; filename="dial.png"
         * String[] tempArr1 =
         * header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
         * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename=
         * "snmp4j--api.zip"}
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
        String[] tempArr1 = header.split(";");
        /**
         * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
         * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
         */
        String[] tempArr2 = tempArr1[2].split("=");
        // 获取文件名，兼容各种浏览器的写法
        String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
        return fileName;
    }
	
	

}
