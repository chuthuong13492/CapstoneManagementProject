/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author dtsang
 */
public class SemesterDAO {
    public List<SemesterDTO> getListSemester() throws SQLException {
        List<SemesterDTO> list = new ArrayList<>();
         Connection conn = null;
         PreparedStatement stm = null;
         ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "SELECT tblSemester.semesterID, tblSemester.semesterName FROM tblSemester ORDER BY tblSemester.NO DESC ";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    String semesterID = rs.getString("semesterID");
                    String semesterName = rs.getString("semesterName");
                    list.add(new SemesterDTO(semesterID, semesterName));
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
//        System.out.println(list);
        return list;
    }
        public int getMaxSemesterNO() throws SQLException {
        int no = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = " SELECT MAX(NO) AS MAXNO "
                        + " FROM tblSemester ";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                if (rs.next()) {
                    no = rs.getInt("MAXNO");
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return no;
    }

    public boolean insertNewSesmester(String semesterID) throws SQLException {
        //get max là SP -> SU -> FA 
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            LocalDateTime localDate = LocalDateTime.now();
            int year = localDate.getYear();
            String yearID = String.valueOf(year);
            String id = yearID.substring(2, 4);
            String test = semesterID.substring(0, 2);
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = " INSERT INTO tblSemester(semesterID , semesterName, NO) "
                        + " VALUES(?,?,?)";
                stm = conn.prepareStatement(sql);
                switch (test) {
                    case "SP":
                        stm.setString(1, "SU" + id);
                        stm.setString(2, "Summer" + yearID);
                        stm.setInt(3, getMaxSemesterNO() + 1);
                        break;
                    case "SU":
                        stm.setString(1, "FA" + id);
                        stm.setString(2, "Fall " + yearID);
                        stm.setInt(3, getMaxSemesterNO() + 1);
                        break;
                    case "FA":
                        stm.setString(1, "SP" + id);
                        stm.setString(2, "Spring " + yearID);
                        stm.setInt(3, getMaxSemesterNO() + 1);
                        break;
                }
                check = stm.executeUpdate() > 0 ? true : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public SemesterDTO getSemester(int no) throws SQLException, ClassNotFoundException {
        SemesterDTO semesterDTO = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = " SELECT semesterID, semesterName"
                    + " FROM tblSemester "
                    + " WHERE NO = ? ";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, no);
            rs = stm.executeQuery();
            if (rs.next()) {
                String semesterID = rs.getString("semesterID");
                String semesterName = rs.getString("semesterName");
                semesterDTO = new SemesterDTO(semesterID, semesterName);
            }
        }finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return semesterDTO;
    }
}
