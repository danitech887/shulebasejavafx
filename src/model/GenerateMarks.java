package model;

import java.util.List;
import java.util.Map;
import model.DBConnection;

public class GenerateMarks{
    private DBConnection db = new DBConnection();
    private String grade;
    private String term;
    private String year;
    
    public GenerateMarks(String grade,String term,String year){
        this.grade = grade;
        this.term = term;
        this.year = year;
    }
    
    public Map<String,Object> getLowerAverageExam(String regno){
            String query = String.format(
                """
                    WITH AverageScores AS (
                    SELECT 
                        m.registration_no AS registration_no,
                        m.grade AS grade,
                        m.stream AS stream,
                        m.term,
                        m.year,
                        AVG(coalesce(m.mathematics,0)) as avg_mathematics,
                        AVG(coalesce(m.english,0)) as avg_english, 
                        AVG(coalesce(m.kiswahili,0)) as avg_kiswahili,
                        AVG(coalesce(m.environmental_activities,0)) as avg_environmental_activities,
                        AVG(coalesce(m.integrated_creative,0)) as avg_integrated_creative,  
                        AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0))  + AVG(coalesce(m.kiswahili,0)) + 
                        AVG(coalesce(m.environmental_activities,0)) + AVG(coalesce(m.integrated_creative,0)) as total_marks,
                        (AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0))  + AVG(coalesce(m.kiswahili,0)) + 
                        AVG(coalesce(m.environmental_activities,0)) + AVG(coalesce(m.integrated_creative,0))) / 5 as total_average
                    FROM marks m
                    WHERE m.type_of_exam IN ('Opener', 'Mid Term','End Term') and m.term = '%s' and m.year = '%s'
                    GROUP BY m.registration_no, m.grade, m.stream
                ),
                RankedStudents AS (
                    SELECT 
                        s.registration_no AS registration_no,
                        s.first_name,
                        s.second_name,
                        s.surname,
                        a.grade,
                        a.term,
                        a.year,
                        a.stream,
                        a.avg_mathematics,
                        a.avg_english,
                        a.avg_kiswahili,
                        a.avg_environmental_activities,
                        a.avg_integrated_creative,
                        (a.avg_mathematics + a.avg_english + a.avg_kiswahili + a.avg_environmental_activities + a.avg_integrated_creative) as total_average_marks,
                        a.total_average,
                        RANK() OVER (PARTITION BY  a.stream ORDER BY a.total_average DESC) as rank_in_stream,RANK() OVER (PARTITION BY a.grade ORDER BY a.total_average DESC) as rank_in_grade
                    FROM students_studentinfo s
                    JOIN AverageScores a ON s.registration_no = a.registration_no where a.grade = '%s' and a.term = '%s' and a.year = '%s'
                )
                SELECT 
                    rs.registration_no,
                    rs.first_name,
                    rs.second_name,
                    rs.surname,
                    CONCAT(rs.first_name, ' ', rs.second_name, ' ', rs.surname) as full_name,
                    rs.grade AS grade,
                    rs.stream AS stream,
                    rs.term,
                    rs.year,
                    rs.avg_mathematics as Mathematics,
                    rs.avg_english as English,
                    rs.avg_kiswahili as Kiswahili,
                    rs.avg_environmental_activities as Environmental_Activities,
                    rs.avg_integrated_creative as Integrated_Creative,
                    rs.total_average_marks as total_marks,
                    rs.total_average as mean_marks,
                    rs.rank_in_stream as position,
                    rs.rank_in_grade as overall_position
                    
                FROM RankedStudents rs
                WHERE rs.grade = '%s' and rs.term = '%s' and rs.year = '%s' and rs.registration_no = '%s'
                ORDER BY total_marks DESC
            """,term,year,grade,term,year,grade,term,year,regno);
        Map<String,Object> average_exams = db.fetchOne(query);
        return average_exams;
    }
    public Map<String,Object> getUpperAverageExam(String regno){
        String query = String.format(
            """
                WITH AverageScores AS (
                    SELECT m.registration_no AS registration_no,
                    m.grade AS grade,
                    m.stream AS stream,
                    m.term,
                    m.year,
                    AVG(coalesce(m.mathematics,0)) as avg_mathematics,
                    AVG(coalesce(m.english,0)) as avg_english, 
                    AVG(coalesce(m.kiswahili,0)) as avg_kiswahili,
                    AVG(coalesce(m.science_technology,0)) as avg_science_technology,
                    AVG(coalesce(m.social_studies,0)) as avg_sst,
                    AVG(coalesce(m.cre,0)) as avg_cre,
                    AVG(coalesce(m.sst_cre,0)) as avg_sst_cre,
                    AVG(coalesce(m.agri_nutrition,0)) as avg_agri_nutrition,
                    AVG(coalesce(m.creative_arts,0)) as avg_creative_arts,
                    AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0)) + AVG(coalesce(m.kiswahili,0)) + AVG(coalesce(m.science_technology,0)) + AVG(coalesce(m.sst_cre,0)) + AVG(coalesce(m.agri_nutrition,0))  + AVG(coalesce(m.creative_arts,0)) as total_marks,(AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0)) + AVG(coalesce(m.kiswahili,0)) + AVG(coalesce(m.science_technology,0)) + AVG(coalesce(m.sst_cre,0)) + AVG(coalesce(m.agri_nutrition,0))  + AVG(coalesce(m.creative_arts,0))) / 7 as mean_average FROM marks m WHERE m.type_of_exam IN ('Opener', 'Mid Term','End Term') and m.term = '%s' and m.year = '%s'
                    GROUP BY m.registration_no,m.grade,m.stream
                ),
                RankedStudents AS (
                    SELECT s.registration_no AS registration_no,
                    s.first_name,
                    s.second_name,
                    s.surname,
                    a.grade,
                    a.stream,
                    a.term,
                    a.year,
                    a.avg_mathematics,
                    a.avg_english,
                    a.avg_kiswahili,
                    a.avg_science_technology,
                    a.avg_sst,
                    a.avg_cre,
                    a.avg_sst_cre,
                    a.avg_agri_nutrition,
                    a.avg_creative_arts,
                    a.total_marks,
                    a.mean_average,
                    RANK() OVER (PARTITION BY a.grade,
                    a.stream ORDER BY a.mean_average DESC) as rank_in_stream,
                    RANK() OVER (PARTITION BY a.grade ORDER BY a.mean_average DESC) as rank_in_grade
                    FROM students_studentinfo s JOIN AverageScores a ON s.registration_no = a.registration_no where a.grade = '%s' and a.term = '%s' and a.year = '%s'
                )
                SELECT rs.registration_no,
                rs.first_name,
                rs.second_name,
                rs.surname,CONCAT(rs.first_name, ' ', rs.second_name, ' ', rs.surname) as full_name,
                rs.grade AS grade,
                rs.stream AS stream,
                rs.term,
                rs.year,
                rs.avg_mathematics as Mathematics,
                rs.avg_english as English,
                rs.avg_kiswahili as Kiswahili,
                rs.avg_science_technology as Science_Technology,
                rs.avg_sst as SST,
                rs.avg_cre as CRE,
                rs.avg_sst_cre as SST_CRE,
                rs.avg_agri_nutrition as Agri_Nutrition,
                rs.avg_creative_arts as Creative_Arts,
                rs.total_marks,
                rs.mean_average as mean_marks,
                rs.rank_in_stream as position,
                rs.rank_in_grade as overall_position FROM RankedStudents rs 
                WHERE rs.grade = '%s'
                and rs.term = '%s' and rs.year = '%s' and rs.registration_no = '%s'
                ORDER BY rs.total_marks DESC
        """,term,year,grade,term,year,grade,term,year,regno
        );
        Map<String,Object> average_exams = db.fetchOne(query);
        return average_exams;
    }
    public Map<String,Object> getJuniorAverageExam(String regno){
        String query = String.format(
            """
                WITH AverageScores AS (
                    SELECT m.registration_no AS registration_no,
                    m.grade AS grade,
                    m.stream AS stream,
                    m.term,
                    m.year,
                    AVG(coalesce(m.mathematics,0)) as avg_mathematics,
                    AVG(coalesce(m.english,0)) as avg_english,
                    AVG(coalesce(m.kiswahili,0)) as avg_kiswahili,
                    AVG(coalesce(m.social_studies,0)) as avg_sst,
                    AVG(coalesce(m.cre,0)) as avg_cre,
                    AVG(coalesce(m.sst_cre,0)) as avg_sst_cre,
                    AVG(coalesce(m.agri_nutrition,0)) as avg_agri_nutrition,
                    AVG(coalesce(m.creative_arts,0)) as avg_creative_arts,
                    AVG(coalesce(m.pretech_bs_computer,0)) as avg_pretech_bs_computer,
                    AVG(coalesce(m.integrated_science,0)) as avg_integrated_science,
                    AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0)) + AVG(coalesce(m.kiswahili,0)) + AVG(coalesce(m.social_studies,0)) + AVG(coalesce(m.sst_cre,0))+ AVG(coalesce(m.agri_nutrition,0)) + AVG(coalesce(m.creative_arts,0)) + AVG(coalesce(m.pretech_bs_computer,0)) + AVG(coalesce(m.integrated_science,0)) as total_marks,
                        (AVG(coalesce(m.mathematics,0)) + AVG(coalesce(m.english,0)) + AVG(coalesce(m.kiswahili,0)) + 
                        AVG(coalesce(m.social_studies,0)) + AVG(coalesce(m.sst_cre,0))+ AVG(coalesce(m.agri_nutrition,0)) + AVG(coalesce(m.creative_arts,0)) + AVG(coalesce(m.pretech_bs_computer,0)) + AVG(coalesce(m.integrated_science,0))) / 8 as mean_average FROM marks m WHERE m.type_of_exam IN ('Opener', 'Mid Term','End Term') and m.term = '%s' and m.year = '%s' GROUP BY m.registration_no, m.grade, m.stream
                ),
                RankedStudents AS (
                    SELECT s.registration_no AS registration_no,
                    s.first_name,
                    s.second_name,
                    s.surname,
                    a.grade,a.stream,
                    a.avg_mathematics,
                    a.avg_english,
                    a.avg_kiswahili,
                    a.avg_sst,
                    a.avg_cre,
                    a.avg_sst_cre,
                    a.avg_agri_nutrition,
                    a.avg_creative_arts,
                    a.avg_pretech_bs_computer,
                    a.avg_integrated_science,
                    a.total_marks,
                    a.mean_average,
                    a.term,
                    a.year,
                    RANK() OVER (PARTITION BY a.stream ORDER BY a.mean_average DESC) as rank_in_stream,
                    RANK() OVER (PARTITION BY a.grade ORDER BY a.mean_average DESC) as rank_in_grade
                FROM students_studentinfo s JOIN AverageScores a ON s.registration_no = a.registration_no
                where a.grade = '%s' and a.term = '%s' and a.year = '%s'
                )
                SELECT rs.registration_no,
                rs.first_name,
                rs.second_name,
                rs.surname,CONCAT(rs.first_name, ' ', rs.second_name, ' ', rs.surname) as full_name,
                rs.grade AS grade,
                rs.stream AS stream,
                rs.term,
                rs.year,
                rs.avg_mathematics as Mathematics,
                rs.avg_english as English,
                rs.avg_kiswahili as Kiswahili,
                rs.avg_sst as SST,
                rs.avg_cre as CRE,
                rs.avg_sst_cre as SST_CRE,
                rs.avg_agri_nutrition as Agri_Nutrition,
                rs.avg_creative_arts as Creative_Arts,
                rs.avg_pretech_bs_computer as Pretech_Bs_Comps,
                rs.avg_integrated_science as Integrated_Science,
                rs.total_marks,
                rs.mean_average as mean_marks,
                rs.rank_in_stream as position,
                rs.rank_in_grade as overall_position
            FROM RankedStudents rs WHERE rs.grade = '%s' and  rs.term = '%s' and rs.year = '%s' and rs.registration_no = '%s'
            ORDER BY rs.total_marks DESC
        """,term,year,grade,term,year,grade,term,year,regno  
        );
        Map<String,Object> average_exams = db.fetchOne(query);
        return average_exams;
    }

    public List<Map<String,Object>> getLowerOpenerExam(String regno){
        List<Map<String,Object>> opener_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(environmental_activities,0) as Environmental_Activities,coalesce(integrated_creative,0) as Integrated_Creative from marks where type_of_exam = 'Opener' and registration_no = '"+regno+"' and term = '"+term+"' and year = '"+year+"'"
        );
        return opener_exams;
    }
    public List<Map<String,Object>> getLowerMidExam(String regno){
        List<Map<String,Object>> mid_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(environmental_activities,0) as Environmental_Activities,coalesce(integrated_creative,0) as Integrated_Creative from marks where type_of_exam = 'Mid Term' and registration_no = '"+regno+"' and term = '"+term+"' and year = '"+year+"'"
        );
        return mid_exams;
    }
    public List<Map<String,Object>> getLowerEndExam(String regno){
        List<Map<String,Object>> end_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(environmental_activities,0) as Environmental_Activities,coalesce(integrated_creative,0) as Integrated_Creative from marks where type_of_exam = 'End Term' and registration_no = '"+regno+"' and term = '"+term+"' and year = '"+year+"'"
        );
        return end_exams;
    }
    public List<Map<String,Object>> getUpperOpenerExam(String regno){
        List<Map<String,Object>> opener_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(science_technology,0) as Science_Technology,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE,coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts from marks where type_of_exam = 'Opener' and registration_no = '"+regno+"'"
        );
        return opener_exams;
    }
    public List<Map<String,Object>> getUpperMidExam(String regno){
        List<Map<String,Object>> mid_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(science_technology,0) as Science_Technology,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE,coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts from marks where type_of_exam = 'Mid Term' and registration_no = '"+regno+"'"
        );
        return mid_exams;
    }
    public List<Map<String,Object>> getUpperEndExam(String regno){
        List<Map<String,Object>> end_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(science_technology,0) as Science_Technology,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE,coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts from marks where type_of_exam = 'End Term' and registration_no = '"+regno+"'"
        );
        return end_exams;
    }
    public List<Map<String,Object>> getJuniorOpenerExam(String regno){
        List<Map<String,Object>> opener_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE, coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts,coalesce(pretech_bs_computer,0) as Pretech_Bs_Comps,coalesce(integrated_science,0) as Integrated_Science from marks where type_of_exam = 'Opener' and registration_no = '"+regno+"'"
        );
        return opener_exams;
    }
    public List<Map<String,Object>> getJuniorMidExam(String regno){
        List<Map<String,Object>> mid_exams  = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE, coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts,coalesce(pretech_bs_computer,0) as Pretech_Bs_Comps,coalesce(integrated_science,0) as Integrated_Science from marks where type_of_exam = 'Mid Term' and registration_no = '"+regno+"'"
        );
        return mid_exams;
    }
    public List<Map<String,Object>> getJuniorEndExam(String regno){
        List<Map<String,Object>> end_exams = db.fetchAll(
            "select coalesce(mathematics,0) as Mathematics,coalesce(english,0) as English,coalesce(kiswahili,0) as Kiswahili,coalesce(social_studies,0) as SST,coalesce(cre,0) as CRE, coalesce(sst_cre,0) as SST_CRE,coalesce(agri_nutrition,0) as Agri_Nutrition,coalesce(creative_arts,0) as Creative_Arts,coalesce(pretech_bs_computer,0) as Pretech_Bs_Comps,coalesce(integrated_science,0) as Integrated_Science from marks where type_of_exam = 'End Term' and registration_no = '"+regno+"'"
        );
        return end_exams;
    }
}
