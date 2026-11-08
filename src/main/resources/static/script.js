const API_URL = "/api";

const loginSection = document.getElementById("loginSection");
const registerSection = document.getElementById("registerSection");
const dashboardSection = document.getElementById("dashboardSection");

const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");

const loginMessage = document.getElementById("loginMessage");
const registerMessage = document.getElementById("registerMessage");

const showRegisterButton = document.getElementById("showRegisterButton");
const showLoginButton = document.getElementById("showLoginButton");

let currentUser = null;
let currentChatRoomId = null;


// ==============================
// API REQUEST HELPER
// ==============================

async function apiRequest(endpoint, method = "GET", body = null) {

    const token = localStorage.getItem("token");

    const headers = {};

    if (body !== null) {
        headers["Content-Type"] = "application/json";
    }

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    const options = {
        method,
        headers
    };

    if (body !== null) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(API_URL + endpoint, options);

    const responseText = await response.text();

    let data = responseText;

    try {
        data = responseText ? JSON.parse(responseText) : null;
    } catch {
        data = responseText;
    }

    if (!response.ok) {
        throw new Error(
            typeof data === "string"
                ? data
                : data?.message || "Something went wrong"
        );
    }

    return data;
}


// ==============================
// PAGE SWITCHING
// ==============================

function showLoginPage() {

    loginSection.hidden = false;
    registerSection.hidden = true;
    dashboardSection.hidden = true;

}

function showRegisterPage() {

    loginSection.hidden = true;
    registerSection.hidden = false;
    dashboardSection.hidden = true;

}

showRegisterButton.addEventListener("click", showRegisterPage);

showLoginButton.addEventListener("click", showLoginPage);


// ==============================
// STUDENT AND TEACHER REGISTRATION
// ==============================

registerForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    registerMessage.textContent = "";

    const role = document.getElementById("registerRole").value;

    const name = document.getElementById("registerName").value.trim();

    const email = document.getElementById("registerEmail").value.trim();

    const password = document.getElementById("registerPassword").value;

    if (!role) {
        registerMessage.textContent = "Please select an account type.";
        return;
    }

    const requestData = {
        name,
        email,
        password
    };

    let endpoint;

    if (role === "student") {
        endpoint = "/auth/register/student";
    } else if (role === "teacher") {
        endpoint = "/auth/register/teacher";
    } else {
        registerMessage.textContent = "Invalid account type.";
        return;
    }

    try {

        const response = await apiRequest(
            endpoint,
            "POST",
            requestData
        );

        registerMessage.textContent =
            typeof response === "string"
                ? response
                : "Registration successful.";

        registerMessage.style.color = "green";

        registerForm.reset();

        if (role === "student") {

            setTimeout(() => {

                showLoginPage();

                loginMessage.textContent =
                    "Registration successful. Please log in.";

            }, 1500);

        } else {

            registerMessage.textContent =
                "Teacher registration successful. Please wait for administrator approval.";

        }

    } catch (error) {

        registerMessage.textContent = error.message;

        registerMessage.style.color = "red";

    }

});


// ==============================
// LOGIN
// ==============================

loginForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    loginMessage.textContent = "";

    const email = document.getElementById("email").value.trim();

    const password = document.getElementById("password").value;

    try {

        const response = await apiRequest(
            "/auth/login",
            "POST",
            {
                email,
                password
            }
        );

        localStorage.setItem("token", response.token);

        localStorage.setItem("userId", response.userId);

        localStorage.setItem("name", response.name);

        localStorage.setItem("role", response.role);

        currentUser = response;

        showDashboard();

    } catch (error) {

        loginMessage.textContent = error.message;

        loginMessage.style.color = "red";

    }

});


// ==============================
// DASHBOARD
// ==============================

function showDashboard() {

    const name = localStorage.getItem("name");

    const role = localStorage.getItem("role");

    if (!localStorage.getItem("token")) {
        showLoginPage();
        return;
    }

    loginSection.hidden = true;

    registerSection.hidden = true;

    dashboardSection.hidden = false;

    document.getElementById("welcomeHeading").textContent =
        `Welcome, ${name}`;

    document.getElementById("userRole").textContent =
        role + " Dashboard";

    loadNavigation(role);

    showHomeDashboard();

}


// ==============================
// NAVIGATION
// ==============================

function loadNavigation(role) {

    const navigation = document.getElementById("sidebarNavigation");

    navigation.innerHTML = "";

    let menuItems = [];

    if (role === "STUDENT") {

        menuItems = [
            ["Dashboard", showHomeDashboard],
            ["My Attendance", loadStudentAttendance],
            ["Announcements", loadAnnouncements],
            ["Calendar", loadCalendar],
            ["My Chatrooms", loadChatrooms]
        ];

    } else if (role === "TEACHER") {

        menuItems = [
            ["Dashboard", showHomeDashboard],
            ["Attendance", loadTeacherAttendance],
            ["Attendance Reports", loadAttendanceReports],
            ["Announcements", loadAnnouncements],
            ["Manage Calendar", loadCalendar],
            ["Manage Chatrooms", loadChatrooms]
        ];

    } else if (role === "ADMIN") {

        menuItems = [
            ["Dashboard", showHomeDashboard],
            ["Pending Teachers", loadPendingTeachers]
        ];

    }

    menuItems.forEach(item => {

        const button = document.createElement("button");

        button.textContent = item[0];

        button.className = "nav-button";

        button.addEventListener("click", item[1]);

        navigation.appendChild(button);

    });

}


// ==============================
// DASHBOARD HOME
// ==============================

function showHomeDashboard() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = `
        <div class="welcome-card">
            <h2>Welcome to School Management System</h2>
            <p>Your school activities, all in one place.</p>
        </div>

        <div class="dashboard-cards" id="dashboardCards"></div>
    `;

    const cards = document.getElementById("dashboardCards");

    const role = localStorage.getItem("role");

    if (role === "STUDENT") {

        createCard(
            cards,
            "My Attendance",
            "Mark your daily attendance and check your history."
        );

        createCard(
            cards,
            "Announcements",
            "View school announcements."
        );

        createCard(
            cards,
            "Calendar",
            "Check upcoming events and holidays."
        );

        createCard(
            cards,
            "My Chatrooms",
            "Communicate with your classmates."
        );

    } else if (role === "TEACHER") {

        createCard(
            cards,
            "Attendance",
            "View student attendance."
        );

        createCard(
            cards,
            "Attendance Reports",
            "Generate attendance reports."
        );

        createCard(
            cards,
            "Announcements",
            "Create and manage announcements."
        );

        createCard(
            cards,
            "Calendar",
            "Manage school events and holidays."
        );

        createCard(
            cards,
            "Chatrooms",
            "Create chatrooms and assign students."
        );

    } else if (role === "ADMIN") {

        createCard(
            cards,
            "Teacher Approval",
            "Review pending teacher registrations."
        );

    }

}


// ==============================
// REUSABLE CARD
// ==============================

function createCard(container, title, description) {

    const card = document.createElement("div");

    card.className = "dashboard-card";

    const heading = document.createElement("h3");

    heading.textContent = title;

    const paragraph = document.createElement("p");

    paragraph.textContent = description;

    card.appendChild(heading);

    card.appendChild(paragraph);

    container.appendChild(card);

    return card;

}


// ==============================
// REUSABLE MESSAGE
// ==============================

function addMessage(container, message, color = "black") {

    const paragraph = document.createElement("p");

    paragraph.textContent = message;

    paragraph.style.color = color;

    container.appendChild(paragraph);

}


// ==============================
// REUSABLE BUTTON
// ==============================

function createButton(text, callback) {

    const button = document.createElement("button");

    button.textContent = text;

    button.addEventListener("click", callback);

    return button;

}


// ==============================
// STUDENT ATTENDANCE
// ==============================

async function loadStudentAttendance() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>My Attendance</h2>";

    const card = createCard(
        content,
        "Mark Today's Attendance",
        "Select your attendance status."
    );

    const status = document.createElement("p");

    status.id = "attendanceStatus";

    card.appendChild(status);

    const presentButton = createButton(
        "Mark Present",
        () => markAttendance("PRESENT")
    );

    card.appendChild(presentButton);

    const historyCard = createCard(
        content,
        "Attendance History",
        "Your attendance records."
    );

    try {

        const records = await apiRequest("/attendance/my");

        if (!records || records.length === 0) {

            addMessage(historyCard, "No attendance records found.");

            return;

        }

        records.forEach(record => {

            addMessage(
                historyCard,
                `${record.date} - ${record.status}`
            );

        });

    } catch (error) {

        addMessage(historyCard, error.message, "red");

    }

}


async function markAttendance(attendanceStatus) {

    try {

        await apiRequest(
            `/attendance/mark?status=${attendanceStatus}`,
            "POST"
        );

        alert("Attendance marked successfully!");

        loadStudentAttendance();

    } catch (error) {

        alert(error.message);

    }

}


// ==============================
// TEACHER ATTENDANCE
// ==============================

async function loadTeacherAttendance() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Student Attendance</h2>";

    const input = document.createElement("input");

    input.type = "date";

    input.id = "attendanceDate";

    input.value = getTodayDate();

    content.appendChild(input);

    const button = createButton(
        "View Attendance",
        getTeacherAttendance
    );

    content.appendChild(button);

    const result = document.createElement("div");

    result.id = "teacherAttendanceResult";

    content.appendChild(result);

    await getTeacherAttendance();

}


async function getTeacherAttendance() {

    const date = document.getElementById("attendanceDate").value;

    const result = document.getElementById("teacherAttendanceResult");

    result.innerHTML = "";

    try {

        const records = await apiRequest(
            `/attendance/date?date=${date}`
        );

        if (!records || records.length === 0) {

            addMessage(result, "No attendance records found.");

            return;

        }

        records.forEach(record => {

            const card = createCard(
                result,
                record.studentName,
                `Date: ${record.date} | Status: ${record.status}`
            );

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


// ==============================
// ATTENDANCE REPORTS
// ==============================

async function loadAttendanceReports() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Attendance Reports</h2>";

    const startDate = document.createElement("input");

    startDate.type = "date";

    startDate.id = "reportStartDate";

    const endDate = document.createElement("input");

    endDate.type = "date";

    endDate.id = "reportEndDate";

    content.appendChild(startDate);

    content.appendChild(endDate);

    content.appendChild(
        createButton("Generate Report", generateAttendanceReports)
    );

    const result = document.createElement("div");

    result.id = "attendanceReportResult";

    content.appendChild(result);

}


async function generateAttendanceReports() {

    const startDate = document.getElementById("reportStartDate").value;

    const endDate = document.getElementById("reportEndDate").value;

    const result = document.getElementById("attendanceReportResult");

    result.innerHTML = "";

    if (!startDate || !endDate) {

        addMessage(result, "Please select both dates.", "red");

        return;

    }

    try {

        const reports = await apiRequest(
            `/attendance/reports/all?startDate=${startDate}&endDate=${endDate}`
        );

        if (!reports || reports.length === 0) {

            addMessage(result, "No reports found.");

            return;

        }

        reports.forEach(report => {

            createCard(
                result,
                report.studentName,
                `Total Days: ${report.totalDays}
                | Present: ${report.presentDays}
                | Absent: ${report.absentDays}
                | Percentage: ${report.attendancePercentage}%`
            );

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


// ==============================
// ANNOUNCEMENTS
// ==============================

async function loadAnnouncements() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Announcements</h2>";

    const role = localStorage.getItem("role");

    if (role === "TEACHER") {

        content.appendChild(
            createButton("Create Announcement", showAnnouncementForm)
        );

    }

    const result = document.createElement("div");

    content.appendChild(result);

    try {

        const announcements = await apiRequest("/announcements");

        if (!announcements || announcements.length === 0) {

            addMessage(result, "No announcements available.");

            return;

        }

        announcements.forEach(item => {

            const card = createCard(
                result,
                item.title,
                item.message
            );

            addMessage(
                card,
                `Posted by: ${item.teacherName}`
            );

            addMessage(
                card,
                `Date: ${item.createdAt}`
            );

            if (role === "TEACHER") {

                card.appendChild(
                    createButton("Delete", async () => {

                        if (!confirm("Delete this announcement?")) return;

                        try {

                            await apiRequest(
                                `/announcements/${item.id}`,
                                "DELETE"
                            );

                            loadAnnouncements();

                        } catch (error) {

                            alert(error.message);

                        }

                    })
                );

            }

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


function showAnnouncementForm() {

    const content = document.getElementById("dashboardContent");

    const card = createCard(
        content,
        "New Announcement",
        "Enter announcement details."
    );

    const form = document.createElement("form");

    form.innerHTML = `
        <input type="text" id="announcementTitle"
            placeholder="Announcement title" required>

        <textarea id="announcementMessage"
            placeholder="Announcement message" required></textarea>

        <button type="submit">Publish</button>
    `;

    form.addEventListener("submit", async function(event) {

        event.preventDefault();

        try {

            await apiRequest(
                "/announcements",
                "POST",
                {
                    title: document.getElementById("announcementTitle").value,
                    message: document.getElementById("announcementMessage").value
                }
            );

            alert("Announcement published!");

            loadAnnouncements();

        } catch (error) {

            alert(error.message);

        }

    });

    card.appendChild(form);

}


// ==============================
// CALENDAR
// ==============================

async function loadCalendar() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>School Calendar</h2>";

    const role = localStorage.getItem("role");

    if (role === "TEACHER") {

        content.appendChild(
            createButton("Add Calendar Event", showCalendarForm)
        );

    }

    const result = document.createElement("div");

    content.appendChild(result);

    try {

        const events = await apiRequest("/calendar");

        if (!events || events.length === 0) {

            addMessage(result, "No calendar events available.");

            return;

        }

        events.forEach(item => {

            const card = createCard(
                result,
                item.title,
                item.description
            );

            addMessage(card, `Date: ${item.eventDate}`);

            addMessage(card, `Type: ${item.eventType}`);

            addMessage(card, `Created by: ${item.teacherName}`);

            if (role === "TEACHER") {

                card.appendChild(
                    createButton("Edit", () => showCalendarForm(item))
                );

                card.appendChild(
                    createButton("Delete", async () => {

                        if (!confirm("Delete this event?")) return;

                        try {

                            await apiRequest(
                                `/calendar/${item.id}`,
                                "DELETE"
                            );

                            loadCalendar();

                        } catch (error) {

                            alert(error.message);

                        }

                    })
                );

            }

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


function showCalendarForm(eventData = null) {

    const content = document.getElementById("dashboardContent");

    const card = createCard(
        content,
        eventData ? "Edit Event" : "Create Event",
        "Enter calendar event details."
    );

    const form = document.createElement("form");

    form.innerHTML = `
        <input type="text" id="eventTitle"
            placeholder="Event title" required>

        <textarea id="eventDescription"
            placeholder="Description"></textarea>

        <input type="date" id="eventDate" required>

        <select id="eventType" required>
            <option value="HOLIDAY">Holiday</option>
            <option value="EXAM">Exam</option>
            <option value="EVENT">Event</option>
            <option value="OTHER">Other</option>
        </select>

        <button type="submit">Save Event</button>
    `;

    card.appendChild(form);

    if (eventData) {

        document.getElementById("eventTitle").value = eventData.title;

        document.getElementById("eventDescription").value =
            eventData.description || "";

        document.getElementById("eventDate").value = eventData.eventDate;

        document.getElementById("eventType").value = eventData.eventType;

    }

    form.addEventListener("submit", async function(event) {

        event.preventDefault();

        const data = {
            title: document.getElementById("eventTitle").value,
            description: document.getElementById("eventDescription").value,
            eventDate: document.getElementById("eventDate").value,
            eventType: document.getElementById("eventType").value
        };

        try {

            if (eventData) {

                await apiRequest(
                    `/calendar/${eventData.id}`,
                    "PUT",
                    data
                );

            } else {

                await apiRequest("/calendar", "POST", data);

            }

            alert("Calendar event saved successfully!");

            loadCalendar();

        } catch (error) {

            alert(error.message);

        }

    });

}


// ==============================
// CHATROOMS
// ==============================

async function loadChatrooms() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Chatrooms</h2>";

    const role = localStorage.getItem("role");

    const endpoint = role === "TEACHER"
        ? "/chatrooms/teacher/my"
        : "/chatrooms/student/my";

    if (role === "TEACHER") {

        content.appendChild(
            createButton("Create Chatroom", showChatroomForm)
        );

    }

    const result = document.createElement("div");

    content.appendChild(result);

    try {

        const rooms = await apiRequest(endpoint);

        if (!rooms || rooms.length === 0) {

            addMessage(result, "No chatrooms available.");

            return;

        }

        rooms.forEach(room => {

            const card = createCard(
                result,
                room.name,
                room.description
            );

            addMessage(card, `Created by: ${room.teacherName}`);

            if (role === "TEACHER") {

                addMessage(
                    card,
                    `Assigned students: ${(room.studentNames || []).join(", ")}`
                );

                const assignForm = document.createElement("form");

                assignForm.innerHTML = `
                    <input type="number"
                        placeholder="Student database ID"
                        class="studentIdInput"
                        required>

                    <button type="submit">Assign Student</button>
                `;

                assignForm.addEventListener("submit", async function(event) {

                    event.preventDefault();

                    const studentId =
                        assignForm.querySelector(".studentIdInput").value;

                    try {

                        await apiRequest(
                            `/chatrooms/${room.id}/students/${studentId}`,
                            "PUT"
                        );

                        alert("Student assigned successfully!");

                        loadChatrooms();

                    } catch (error) {

                        alert(error.message);

                    }

                });

                card.appendChild(assignForm);

            }

            card.appendChild(
                createButton("Open Chat", () => openChatroom(room.id))
            );

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


function showChatroomForm() {

    const content = document.getElementById("dashboardContent");

    const card = createCard(
        content,
        "Create Chatroom",
        "Enter chatroom details."
    );

    const form = document.createElement("form");

    form.innerHTML = `
        <input type="text" id="chatroomName"
            placeholder="Chatroom name" required>

        <textarea id="chatroomDescription"
            placeholder="Description"></textarea>

        <button type="submit">Create</button>
    `;

    card.appendChild(form);

    form.addEventListener("submit", async function(event) {

        event.preventDefault();

        try {

            await apiRequest(
                "/chatrooms",
                "POST",
                {
                    name: document.getElementById("chatroomName").value,
                    description: document.getElementById("chatroomDescription").value
                }
            );

            alert("Chatroom created successfully!");

            loadChatrooms();

        } catch (error) {

            alert(error.message);

        }

    });

}


// ==============================
// CHAT MESSAGES
// ==============================

async function openChatroom(roomId) {

    currentChatRoomId = roomId;

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Chatroom Messages</h2>";

    content.appendChild(
        createButton("Refresh Messages", () => loadChatMessages(roomId))
    );

    const messages = document.createElement("div");

    messages.id = "chatMessages";

    messages.className = "chat-messages";

    content.appendChild(messages);

    const form = document.createElement("form");

    form.innerHTML = `
        <input type="text" id="chatMessageInput"
            placeholder="Write a message..." required>

        <button type="submit">Send</button>
    `;

    content.appendChild(form);

    form.addEventListener("submit", async function(event) {

        event.preventDefault();

        const message =
            document.getElementById("chatMessageInput").value.trim();

        if (!message) return;

        try {

            await apiRequest(
                `/chatrooms/${roomId}/messages`,
                "POST",
                {
                    message
                }
            );

            form.reset();

            loadChatMessages(roomId);

        } catch (error) {

            alert(error.message);

        }

    });

    await loadChatMessages(roomId);

}


async function loadChatMessages(roomId) {

    const container = document.getElementById("chatMessages");

    if (!container) return;

    container.innerHTML = "";

    try {

        const messages = await apiRequest(
            `/chatrooms/${roomId}/messages`
        );

        if (!messages || messages.length === 0) {

            addMessage(container, "No messages yet.");

            return;

        }

        messages.forEach(item => {

            const card = createCard(
                container,
                item.senderName,
                item.message
            );

            addMessage(card, item.sentAt);

        });

    } catch (error) {

        addMessage(container, error.message, "red");

    }

}


// ==============================
// ADMIN - TEACHER APPROVAL
// ==============================

async function loadPendingTeachers() {

    const content = document.getElementById("dashboardContent");

    content.innerHTML = "<h2>Pending Teacher Registrations</h2>";

    const result = document.createElement("div");

    content.appendChild(result);

    try {

        const teachers = await apiRequest("/admin/teachers/pending");

        if (!teachers || teachers.length === 0) {

            addMessage(result, "No pending teacher registrations.");

            return;

        }

        teachers.forEach(teacher => {

            const card = createCard(
                result,
                teacher.name,
                `Email: ${teacher.email}`
            );

            card.appendChild(
                createButton("Approve Teacher", async () => {

                    if (!confirm(`Approve ${teacher.name}?`)) return;

                    try {

                        await apiRequest(
                            `/admin/teachers/${teacher.id}/approve`,
                            "PUT"
                        );

                        alert("Teacher approved successfully!");

                        loadPendingTeachers();

                    } catch (error) {

                        alert(error.message);

                    }

                })
            );

        });

    } catch (error) {

        addMessage(result, error.message, "red");

    }

}


// ==============================
// DATE HELPER
// ==============================

function getTodayDate() {

    const date = new Date();

    const year = date.getFullYear();

    const month = String(date.getMonth() + 1).padStart(2, "0");

    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;

}


// ==============================
// LOGOUT
// ==============================

document.getElementById("logoutButton").addEventListener("click", logout);

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("userId");

    localStorage.removeItem("name");

    localStorage.removeItem("role");

    currentUser = null;

    loginForm.reset();

    loginMessage.textContent = "";

    showLoginPage();

}


// ==============================
// RESTORE LOGIN SESSION
// ==============================

window.addEventListener("DOMContentLoaded", function() {

    const token = localStorage.getItem("token");

    if (token) {

        showDashboard();

    } else {

        showLoginPage();

    }

});