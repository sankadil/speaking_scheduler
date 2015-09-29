
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
public class WavPlayer extends Thread {
	   
	   Player player;
	   static int nowPlayingIndex=0;
	   public static List<String> filenames=new ArrayList<String>() ;
	   
 	   public WavPlayer(String mp3Filename) {
	     // this.filename = mp3Filename;
	   } 
 	   public WavPlayer() {
 		  try {
			this.setDataSource();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	   } 

	   
	   public void run() {
	      try {
	    	  
	    	  if(filenames==null || filenames.size()==0) return;//validation steps
	    	  
	         URL url = this.getClass().getClassLoader().getResource(filenames.get(nowPlayingIndex));
	         MediaLocator locator = new MediaLocator(url);
	         player = Manager.createPlayer(locator);
	         player.addControllerListener(new ControllerListener() {
	            public void controllerUpdate(ControllerEvent event) {
	               if (event instanceof EndOfMediaEvent) {
		      	         if(filenames.size()-1>nowPlayingIndex){
		    	        	 nowPlayingIndex++;
		    	        	 new WavPlayer(filenames.get(nowPlayingIndex)).start();
		    	         }
		                  player.stop();
		                  player.close();
	               }
	            }
	         });
	         player.realize();
	         player.start();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

	   
		public void setDataSource() throws SQLException, ClassNotFoundException {
			
			Connection conn = null;
			Statement sta = null;
			ResultSet rs = null;
			System.out.println("run here");
			try {
//				String url="jdbc:sqlserver://MENAKA-LAPTOP\\SQLEXPRESS:1433;user=sa;password=testing;databaseName=malkey";
				String url=PropertyUtil.getPropertyValue("database");//"jdbc:sqlserver://SERVER:1433;user=sa;password=Testing123;databaseName=malkey;";
				String Sql=" select regno from fresvehicle where priority=1 and resno in ( select resno from freservation where cohirestsid='CONFIRMED' AND DOUT=CONVERT(VARCHAR(10),GETDATE(),120) and CONVERT(VARCHAR(2),GETDATE(),108) <= substring(freservation.TIMEOUT,1,2) and CONVERT(VARCHAR(2),GETDATE(),108)+2 >= substring(freservation.TIMEOUT,1,2))";
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
				conn = DriverManager.getConnection(url);
				sta = conn.createStatement();
				rs = sta.executeQuery(Sql);
				List<String> listVehicles=new ArrayList<String>();
				while (rs.next()) {
					listVehicles.add(rs.getString("regno").replaceAll("[-\\s]", "").trim());
				}
				
				if (listVehicles.size() > 0) {
					this.filenames.add("all.wav");
				}
				
				for (int i = 0; i < listVehicles.size(); i++) {
					System.out.println("regno : "+listVehicles.get(i));
					this.filenames.addAll(this.getPlayList(listVehicles.get(i)));
					if (i < listVehicles.size() - 1) {
						this.filenames.add("and.wav");
					}
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (sta != null) try { sta.close(); } catch(Exception e) {}
				if (conn != null) try { conn.close(); } catch(Exception e) {}
			}
			
		}
		
		
	   public boolean isNumeric(String str)  
	   {  
	     try  
	     {  
	       double d = Double.parseDouble(str);  
	     }  
	     catch(NumberFormatException nfe)  
	     {  
	       return false;  
	     }  
	     return true;  
	   }
	   
	   /***
	    * 
	    * following method 
	    * @param text
	    * @return
	    */
	   public java.util.List<String> getPlayList(String text)
	   {
		 text=text.toUpperCase();
		 String pathPrefixAlphabet= "audio-alphabet/";
		 String pathPrefixNumbers= "audio-numbers/";
		 String pathSufix= ".wav";
		 char[] listCharactor=text.toCharArray();
		 java.util.List<String> listPlayList=new ArrayList<String>();
			for (int i = 0; i < listCharactor.length; i++) {
				
				StringBuilder txtURL=new StringBuilder();
				if(isNumeric(Character.toString(listCharactor[i])))
				{
					txtURL=txtURL.append(pathPrefixNumbers);
				}
				else
				{
					txtURL=txtURL.append(pathPrefixAlphabet);
				}
				
				txtURL=txtURL.append(listCharactor[i]);
				txtURL=txtURL.append(pathSufix);
				listPlayList.add(txtURL.toString());
			}
		   return listPlayList;
	   }
	}



//bellow is the sql for retrive relevent data set from DB

/*
select * from freservation where cohirestsid='CONFIRMED' AND DOUT=CONVERT(VARCHAR(10),GETDATE(),120)
and CONVERT(VARCHAR(2),GETDATE(),108) <= substring(freservation.TIMEOUT,1,2)
and CONVERT(VARCHAR(2),GETDATE(),108)+2 >= substring(freservation.TIMEOUT,1,2)

select regno from fresvehicle where priority=1 and resno in ( select resno from freservation where cohirestsid='CONFIRMED' AND DOUT=CONVERT(VARCHAR(10),GETDATE(),120) and CONVERT(VARCHAR(2),GETDATE(),108) <= substring(freservation.TIMEOUT,1,2) and CONVERT(VARCHAR(2),GETDATE(),108)+2 >= substring(freservation.TIMEOUT,1,2))

*/


/*	   public static void main(String[] args) {
		   Mp3PlayerDemo obj= new Mp3PlayerDemo();
		   obj.filenames.add("all.wav");
		   obj.filenames.addAll(obj.getPlayList("AA7252"));//prepare and add all here
		   obj.filenames.add("and.wav");
		   obj.filenames.addAll(obj.getPlayList("WK0589"));//prepare and add all here
		   //obj.filenames.add("speech2.wav");
		   obj.start();
	      
	   }*/

 /*			
		public static void main(String[] args) throws SQLException, ClassNotFoundException {
			
			Connection conn = null;
			Statement sta = null;
			ResultSet rs = null;
			System.out.println("run here");
			 try {
			String url="jdbc:sqlserver://MENAKA-LAPTOP\\SQLEXPRESS:1433;user=sa;password=testing;databaseName=malkey";
			String Sql=" select regno from fresvehicle where priority=1 and resno in ( select resno from freservation where cohirestsid='CONFIRMED' AND DOUT=CONVERT(VARCHAR(10),GETDATE(),120) and CONVERT(VARCHAR(2),GETDATE(),108) <= substring(freservation.TIMEOUT,1,2) and CONVERT(VARCHAR(2),GETDATE(),108)+2 >= substring(freservation.TIMEOUT,1,2))";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
			conn = DriverManager.getConnection(url);
			sta = conn.createStatement();
			rs = sta.executeQuery(Sql);
			List<String> listVehicles=new ArrayList<String>();
			while (rs.next()) {
				listVehicles.add(rs.getString("regno").replaceAll("[-\\s]", "").trim());
			}
			
			Mp3PlayerDemo obj= new Mp3PlayerDemo();
			if (listVehicles.size() > 0) {
				obj.filenames.add("all.wav");
			}
			
			for (int i = 0; i < listVehicles.size(); i++) {
				System.out.println("regno : "+listVehicles.get(i));
				obj.filenames.addAll(obj.getPlayList(listVehicles.get(i)));
				if (i < listVehicles.size() - 1) {
					obj.filenames.add("and.wav");
				}
			}
 

			 obj.start();
			 
			 }
		      catch (Exception e) {
		         e.printStackTrace();
		      }
		      finally {
		         if (rs != null) try { rs.close(); } catch(Exception e) {}
		         if (sta != null) try { sta.close(); } catch(Exception e) {}
		         if (conn != null) try { conn.close(); } catch(Exception e) {}
		      }
		      
		}
		*/

