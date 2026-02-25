--- /dev/null
+++ c/Users/w/Desktop/Projects/ShuleBaseJava/README.md
@@ -0,0 +1,87 @@
+# ShuleBase - School Management System
+
+ShuleBase is a comprehensive desktop application built with Java and JavaFX designed to streamline school administration. It handles various aspects of school management including student fees, academic records, attendance, leave management, and teacher role assignments.
+
+## Features
+
+### 1. Fee Management (`FeeDashboard`)
+*   **Visual Analytics:** Pie charts for payment modes (Mpesa vs Cash), line charts for monthly fee trends, and bar charts for termly collections.
+*   **Summary Cards:** Quick view of total paid, pending balances, and payment breakdowns.
+*   **Reporting:** Generate detailed fee reports in PDF or CSV formats based on grade, stream, and term.
+
+### 2. Academic Management (`AcademicDashboard`)
+*   **Performance Tracking:** View top students and analyze exam performance across different grades.
+*   **Report Forms:** Automated generation of student report forms for:
+    *   **Lower Primary:** PP1 - Grade 3
+    *   **Upper Primary:** Grade 4 - Grade 6
+    *   **Junior Secondary:** Grade 7 - Grade 9
+*   **Bulk Generation:** Merge and export all report forms for a specific stream or grade into a single PDF.
+
+### 3. Attendance Tracking (`AttendanceDashboard`)
+*   **Daily Monitoring:** Track student presence and absence.
+*   **Analytics:** Visual breakdown of attendance by gender, grade, and term.
+*   **Reports:** Generate attendance reports for specific classes and terms.
+
+### 4. Leave Management (`LeaveDashboard` & `ManageLeave`)
+*   **Leave Requests:** Record student leave with specific reasons (e.g., School Fees, Medical).
+*   **Gate Passes:** Generate PDF leave receipts/gate passes automatically upon approval.
+*   **Statistics:** Analyze leave trends by gender and reason (Pie charts and Bar charts).
+*   **Statement:** View and update leave history (e.g., marking return dates).
+
+### 5. Teacher Roles (`Roles`)
+*   **Role Assignment:** Assign Class Teacher roles and specific subjects to teachers.
+*   **Curriculum Support:** Supports subject selection for CBC levels (Lower, Upper, and Junior Secondary).
+*   **Search:** Filter teachers by registration number, grade, or stream.
+
+## Technical Stack
+
+*   **Language:** Java
+*   **UI Framework:** JavaFX
+*   **Database:** MySQL
+*   **Reporting:** PDF Generation (iText or similar library implied)
+*   **File Handling:** Custom directory management for organizing reports and receipts.
+
+## Database Schema
+
+The project uses a MySQL database defined in `shulebase.sql`. Key tables include:
+*   `students_studentinfo`: Student personal details.
+*   `students_fee`: Fee transaction records.
+*   `students_marks`: Academic scores.
+*   `students_leavemanagement`: Leave records.
+*   `students_studentattendance`: Daily attendance logs.
+*   `students_teachersrole`: Teacher subject and class assignments.
+
+## Setup and Installation
+
+1.  **Prerequisites:**
+    *   Java Development Kit (JDK) 8 or higher (with JavaFX support).
+    *   MySQL Server.
+
+2.  **Database Setup:**
+    *   Create a database in MySQL.
+    *   Import the `shulebase.sql` file provided in the root directory to create the necessary tables.
+
+3.  **Configuration:**
+    *   Ensure the `model.DBConnection` class (not shown in snippets but referenced) is configured with your local database credentials (URL, Username, Password).
+
+4.  **Running the Application:**
+    *   Compile and run the project starting from the main entry point (likely in `src/main` or similar, invoking the JavaFX `Application.launch`).
+
+## Directory Structure
+
+The application automatically creates a file structure for outputs in `ShuleBase Files`:
+*   `/Fee Reports`: Stores generated fee statements.
+*   `/Report Forms`: Stores student academic report cards.
+*   `/Leave Receipts`: Stores gate passes for students.
+*   `/Attendance Reports`: Stores attendance logs.
+
+## Usage
+
+*   **Dashboards:** Navigate through the application to view specific dashboards for Fees, Academics, etc.
+*   **Exporting:** Look for "Generate Report" or "Download" buttons in the respective modules to create PDF/CSV files.
+*   **Approvals:** Use the `Roles` and `ManageLeave` views to input data and approve requests.
+
+## License
+
+Proprietary / Custom License.
