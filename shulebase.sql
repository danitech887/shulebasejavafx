-- create_tables.sql

SET FOREIGN_KEY_CHECKS=0;

-- --------------------------------------------------------
-- Table structure for MasterSchool
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_masterschool` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `school_name` VARCHAR(100) NOT NULL,
  `po_box` VARCHAR(50) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `contact` VARCHAR(20) NOT NULL,
  `email_address` VARCHAR(254) NOT NULL,
  `logo_path` VARCHAR(100),
  `created_at` DATETIME(6) NOT NULL,
  `motto` VARCHAR(255),
  `vision` LONGTEXT,
  `status` VARCHAR(20) NOT NULL DEFAULT 'Pending'
);

-- --------------------------------------------------------
-- Table structure for RegConfig
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_regconfig` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `reg_format` VARCHAR(10) NOT NULL,
  `target_type` VARCHAR(10) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_regconfig_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for Users
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `password` VARCHAR(128) NOT NULL,
  `last_login` DATETIME(6),
  `is_superuser` TINYINT(1) NOT NULL DEFAULT 0,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `school_name` VARCHAR(100) NOT NULL,
  `role` VARCHAR(30) NOT NULL,
  `registration_no` VARCHAR(30) NOT NULL,
  `email` VARCHAR(254),
  `created_at` DATETIME(6) NOT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `is_staff` TINYINT(1) NOT NULL DEFAULT 0,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_users_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for StudentInfo
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_studentinfo` (
  `registration_no` VARCHAR(20) NOT NULL PRIMARY KEY,
  `first_name` VARCHAR(20) NOT NULL,
  `second_name` VARCHAR(20) NOT NULL,
  `surname` VARCHAR(20) NOT NULL,
  `gender` VARCHAR(10) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `date_of_registration` DATE,
  `parent_name` VARCHAR(20),
  `grade` VARCHAR(10) NOT NULL,
  `stream` VARCHAR(10) NOT NULL,
  `phone` BIGINT NOT NULL,
  `email` VARCHAR(50),
  `address` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  `parent_portal_user_id` INT,
  CONSTRAINT `fk_studentinfo_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_studentinfo_parent` FOREIGN KEY (`parent_portal_user_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL,
  UNIQUE KEY `unique_student_school_reg` (`school_id`, `registration_no`)
);

-- --------------------------------------------------------
-- Table structure for Fee
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_fee` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `mode_of_payment` VARCHAR(10),
  `transaction_code` VARCHAR(30),
  `amount` DECIMAL(10, 2),
  `date_of_payment` DATE,
  `time` TIME(6),
  `term` VARCHAR(6),
  `year` INT,
  `status` VARCHAR(10) NOT NULL DEFAULT 'Pending',
  `registration_no_id` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_fee_student` FOREIGN KEY (`registration_no_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_fee_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for LeaveManagement
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_leavemanagement` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `reason` VARCHAR(50),
  `other_reason` VARCHAR(50),
  `term` VARCHAR(10) NOT NULL,
  `status` VARCHAR(10),
  `phone` BIGINT NOT NULL,
  `date_of_leave` DATE,
  `return_date` DATE,
  `year` INT,
  `time` TIME(6),
  `registration_no_id` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_leave_student` FOREIGN KEY (`registration_no_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_leave_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for Marks
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_marks` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `mathematics` INT,
  `english` INT,
  `kiswahili` INT,
  `science_technology` INT,
  `social_studies` INT,
  `cre` INT,
  `sst_cre` INT,
  `agri_nutrition` INT,
  `creative_arts` INT,
  `pretech_bs_computer` INT,
  `integrated_science` INT,
  `environmental_activities` INT,
  `integrated_creative` INT,
  `total_marks` INT,
  `mean_marks` DOUBLE,
  `term` VARCHAR(10),
  `type_of_exam` VARCHAR(10),
  `year` INT,
  `registration_no_id` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_marks_student` FOREIGN KEY (`registration_no_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_marks_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for LearningArea
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_learningarea` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `grade_level` VARCHAR(20) NOT NULL,
  `category` VARCHAR(50),
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_learningarea_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_learningarea` (`name`, `grade_level`, `school_id`)
);

-- --------------------------------------------------------
-- Table structure for Strand
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_strand` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `grade` VARCHAR(10) NOT NULL,
  `stream` VARCHAR(10) NOT NULL,
  `subject` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` LONGTEXT,
  `created_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL,
  `created_by_id` INT,
  CONSTRAINT `fk_strand_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_strand_user` FOREIGN KEY (`created_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Table structure for SubStrand
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_substrand` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `description` LONGTEXT,
  `created_at` DATETIME(6) NOT NULL,
  `strand_id` INT NOT NULL,
  `created_by_id` INT,
  CONSTRAINT `fk_substrand_strand` FOREIGN KEY (`strand_id`) REFERENCES `students_strand` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_substrand_user` FOREIGN KEY (`created_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Table structure for SubStrandMark
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_substrandmark` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `term` VARCHAR(20) NOT NULL,
  `year` INT NOT NULL,
  `raw_score` DOUBLE NOT NULL DEFAULT 0,
  `score` DOUBLE NOT NULL,
  `remark` VARCHAR(300) NOT NULL DEFAULT 'Good',
  `level` SMALLINT UNSIGNED NOT NULL DEFAULT 1,
  `assessment_type` VARCHAR(100) NOT NULL DEFAULT 'SBA-Written',
  `date_recorded` DATETIME(6) NOT NULL,
  `student_id` VARCHAR(20) NOT NULL,
  `sub_strand_id` INT NOT NULL,
  `recorded_by_id` INT,
  CONSTRAINT `fk_substrandmark_student` FOREIGN KEY (`student_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_substrandmark_substrand` FOREIGN KEY (`sub_strand_id`) REFERENCES `students_substrand` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_substrandmark_user` FOREIGN KEY (`recorded_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL,
  UNIQUE KEY `unique_substrandmark` (`student_id`, `sub_strand_id`, `term`, `year`)
);

-- --------------------------------------------------------
-- Table structure for AssessmentEvent
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_assessmentevent` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `date` DATE NOT NULL,
  `term` VARCHAR(20) NOT NULL,
  `year` INT NOT NULL,
  `grade` VARCHAR(20) NOT NULL,
  `stream` VARCHAR(20),
  `subject` VARCHAR(100) NOT NULL,
  `activity` VARCHAR(255) NOT NULL,
  `method` VARCHAR(100),
  `school_id` INT NOT NULL,
  `created_by_id` INT,
  `strand_id` INT,
  `sub_strand_id` INT,
  CONSTRAINT `fk_assessmentevent_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_assessmentevent_user` FOREIGN KEY (`created_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_assessmentevent_strand` FOREIGN KEY (`strand_id`) REFERENCES `students_strand` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_assessmentevent_substrand` FOREIGN KEY (`sub_strand_id`) REFERENCES `students_substrand` (`id`) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Table structure for OutcomeDescriptor
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_outcomedescriptor` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `level` SMALLINT UNSIGNED NOT NULL,
  `descriptor` LONGTEXT NOT NULL,
  `sub_strand_id` INT NOT NULL,
  CONSTRAINT `fk_outcomedescriptor_substrand` FOREIGN KEY (`sub_strand_id`) REFERENCES `students_substrand` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_outcomedescriptor` (`sub_strand_id`, `level`)
);

-- --------------------------------------------------------
-- Table structure for Observation
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_observation` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `term` VARCHAR(20) NOT NULL,
  `year` INT NOT NULL,
  `level` SMALLINT UNSIGNED NOT NULL,
  `comment` VARCHAR(300),
  `evidence_link` VARCHAR(200),
  `recorded_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL,
  `student_id` VARCHAR(20) NOT NULL,
  `sub_strand_id` INT NOT NULL,
  `event_id` INT,
  `recorded_by_id` INT,
  CONSTRAINT `fk_observation_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_observation_student` FOREIGN KEY (`student_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_observation_substrand` FOREIGN KEY (`sub_strand_id`) REFERENCES `students_substrand` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_observation_event` FOREIGN KEY (`event_id`) REFERENCES `students_assessmentevent` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_observation_user` FOREIGN KEY (`recorded_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Table structure for LearnerCompetency
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_learnercompetency` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `grade` VARCHAR(20) NOT NULL,
  `stream` VARCHAR(20) NOT NULL,
  `subject` VARCHAR(100) NOT NULL,
  `term` VARCHAR(20) NOT NULL,
  `year` INT NOT NULL,
  `competency` VARCHAR(100) NOT NULL,
  `level` SMALLINT UNSIGNED NOT NULL,
  `comment` VARCHAR(300),
  `recorded_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL,
  `student_id` VARCHAR(20) NOT NULL,
  `recorded_by_id` INT,
  CONSTRAINT `fk_learnercompetency_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_learnercompetency_student` FOREIGN KEY (`student_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_learnercompetency_user` FOREIGN KEY (`recorded_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL,
  UNIQUE KEY `unique_learnercompetency` (`student_id`, `term`, `year`, `competency`)
);

-- --------------------------------------------------------
-- Table structure for StudentAttendance
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_studentattendance` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `date_of_attendance` DATE,
  `term` VARCHAR(20),
  `time` TIME(6),
  `year` INT,
  `status` VARCHAR(10),
  `registration_no_id` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_studentattendance_student` FOREIGN KEY (`registration_no_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_studentattendance_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for TeacherInfo
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_teacherinfo` (
  `registration_no` VARCHAR(20) NOT NULL PRIMARY KEY,
  `first_name` VARCHAR(30) NOT NULL,
  `second_name` VARCHAR(30) NOT NULL,
  `surname` VARCHAR(30) NOT NULL,
  `gender` VARCHAR(10) NOT NULL,
  `date_of_registration` DATE,
  `phone` VARCHAR(20) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_teacherinfo_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_teacher_school_reg` (`school_id`, `registration_no`)
);

-- --------------------------------------------------------
-- Table structure for TeacherAttendance
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_teacherattendance` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `date_of_attendance` DATE,
  `term` VARCHAR(6),
  `year` INT,
  `status` VARCHAR(7),
  `time_in` TIME(6),
  `registration_no` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_teacherattendance_teacher` FOREIGN KEY (`registration_no`) REFERENCES `students_teacherinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_teacherattendance_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for TeachersRole
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_teachersrole` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `type_of_teacher` VARCHAR(20),
  `grade` VARCHAR(10),
  `stream` VARCHAR(10),
  `subject` VARCHAR(200),
  `registration_no` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_teachersrole_teacher` FOREIGN KEY (`registration_no`) REFERENCES `students_teacherinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_teachersrole_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for TeachingProgress
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_teachingprogress` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `grade` VARCHAR(20) NOT NULL,
  `stream` VARCHAR(20) NOT NULL,
  `subject` VARCHAR(355),
  `no_of_topics` INT,
  `topic` VARCHAR(500),
  `sub_topic` VARCHAR(500),
  `status` VARCHAR(9),
  `date_of_teaching` DATETIME(6),
  `date_finished` DATE,
  `registration_no` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_teachingprogress_teacher` FOREIGN KEY (`registration_no`) REFERENCES `students_teacherinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_teachingprogress_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for LoginDetails
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `logindetails` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(20) UNIQUE,
  `type_of_user` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `teacher_no` VARCHAR(20) NOT NULL,
  CONSTRAINT `fk_logindetails_teacher` FOREIGN KEY (`teacher_no`) REFERENCES `students_teacherinfo` (`registration_no`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for GradeStream
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_gradestream` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `grade` VARCHAR(20) NOT NULL,
  `stream` VARCHAR(20) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_gradestream_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_gradestream` (`school_id`, `grade`, `stream`)
);

-- --------------------------------------------------------
-- Table structure for Grade
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_grade` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `expected_fee` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  `term` VARCHAR(10) NOT NULL DEFAULT 'Term 1',
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_grade_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_grade` (`school_id`, `name`)
);

-- --------------------------------------------------------
-- Table structure for ContactMessage
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_contactmessage` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `subject` VARCHAR(200) NOT NULL DEFAULT '',
  `message` LONGTEXT NOT NULL,
  `created_at` DATETIME(6) NOT NULL
);

-- --------------------------------------------------------
-- Table structure for Announcement
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_announcement` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `target_audience` VARCHAR(50) NOT NULL DEFAULT 'All',
  `created_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_announcement_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for Testimonial
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_testimonial` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `full_name` VARCHAR(100) NOT NULL,
  `user_type` VARCHAR(20) NOT NULL,
  `location` VARCHAR(100) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `rating` INT NOT NULL DEFAULT 5,
  `is_approved` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL,
  CONSTRAINT `fk_testimonial_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table structure for Resource
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_resource` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(255) NOT NULL,
  `subject` VARCHAR(100) NOT NULL,
  `grade` VARCHAR(10) NOT NULL,
  `stream` VARCHAR(19) NOT NULL DEFAULT 'A',
  `term` VARCHAR(10) NOT NULL DEFAULT 'Term 3',
  `year` INT NOT NULL,
  `resource_type` VARCHAR(10) NOT NULL,
  `url_or_file` VARCHAR(500) NOT NULL,
  `thumbnail` VARCHAR(100),
  `created_at` DATETIME(6) NOT NULL,
  `school_id` INT NOT NULL DEFAULT 1,
  `recorded_by_id` INT,
  CONSTRAINT `fk_resource_school` FOREIGN KEY (`school_id`) REFERENCES `students_masterschool` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_resource_user` FOREIGN KEY (`recorded_by_id`) REFERENCES `students_users` (`id`) ON DELETE SET NULL
);

-- --------------------------------------------------------
-- Table structure for StudentNote
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_studentnote` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `content` LONGTEXT NOT NULL,
  `updated_at` DATETIME(6) NOT NULL,
  `student_id` VARCHAR(20) NOT NULL,
  `resource_id` INT NOT NULL,
  CONSTRAINT `fk_studentnote_student` FOREIGN KEY (`student_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_studentnote_resource` FOREIGN KEY (`resource_id`) REFERENCES `students_resource` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_studentnote` (`student_id`, `resource_id`)
);

-- --------------------------------------------------------
-- Table structure for LearningLog
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_learninglog` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `progress` INT NOT NULL DEFAULT 0,
  `watched_at` DATETIME(6) NOT NULL,
  `completed` TINYINT(1) NOT NULL DEFAULT 0,
  `student_id` VARCHAR(20) NOT NULL,
  `resource_id` INT NOT NULL,
  CONSTRAINT `fk_learninglog_student` FOREIGN KEY (`student_id`) REFERENCES `students_studentinfo` (`registration_no`) ON DELETE CASCADE,
  CONSTRAINT `fk_learninglog_resource` FOREIGN KEY (`resource_id`) REFERENCES `students_resource` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `unique_learninglog` (`student_id`, `resource_id`)
);

SET FOREIGN_KEY_CHECKS=1;
