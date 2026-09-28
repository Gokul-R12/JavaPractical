const API = "/api";

let rooms = [];
let guests = [];
let bookings = [];


/* =========================
   PAGE NAVIGATION
========================= */

function showPage(pageName, clickedButton) {

    const pages = document.querySelectorAll(".page");

    pages.forEach(page => {
        page.classList.remove("active-page");
    });


    const selectedPage =
        document.getElementById(pageName + "Page");

    if (selectedPage) {
        selectedPage.classList.add("active-page");
    }


    const buttons =
        document.querySelectorAll(".nav-button");

    buttons.forEach(button => {
        button.classList.remove("active");
    });


    if (clickedButton) {
        clickedButton.classList.add("active");
    }
}


/* =========================
   LOAD ALL DATA
========================= */

async function loadData() {

    try {

        const roomResponse =
            await fetch(API + "/rooms");

        rooms = await roomResponse.json();


        const guestResponse =
            await fetch(API + "/guests");

        guests = await guestResponse.json();


        const bookingResponse =
            await fetch(API + "/bookings");

        bookings = await bookingResponse.json();


        updateDashboard();

        displayRooms(rooms);

        displayGuests();

        displayBookings();

        displayDashboardBookings();

        //generateOccupancyReport();


    } catch (error) {

        console.error(
            "Error loading data:",
            error
        );

    }
}


/* =========================
   DASHBOARD COUNTS
========================= */

function updateDashboard() {

    document.getElementById("totalRooms").textContent =
        rooms.length;

    document.getElementById("totalGuests").textContent =
        guests.length;

    document.getElementById("totalBookings").textContent =
        bookings.length;
}


/* =========================
   ROOM STATUS
========================= */

function getRoomStatus(room) {

    const roomBookings =
        bookings.filter(booking =>

            booking.room &&

            booking.room.roomId === room.roomId &&

            booking.status === "CONFIRMED"
        );


    if (roomBookings.length > 0) {

        return {
            text: "Occupied",
            className: "status-occupied"
        };

    }


    return {
        text: "Available",
        className: "status-available"
    };
}


/* =========================
   DISPLAY ROOMS
========================= */

function displayRooms(roomList) {

    const dashboardTable =
        document.getElementById("roomTable");

    const roomsPageTable =
        document.getElementById("roomsPageTable");


    if (dashboardTable) {
        dashboardTable.innerHTML = "";
    }

    if (roomsPageTable) {
        roomsPageTable.innerHTML = "";
    }


    roomList.forEach(room => {

        const status =
            getRoomStatus(room);


        const dashboardRow = `
            <tr>
                <td>${room.roomNumber}</td>
                <td>${room.type}</td>
                <td>${room.capacity}</td>
                <td>₹${room.ratePerNight}</td>
                <td class="${status.className}">
                    ${status.text}
                </td>
            </tr>
        `;


        const roomRow = `
            <tr>
                <td>${room.roomNumber}</td>
                <td>${room.type}</td>
                <td>${room.capacity}</td>
                <td>₹${room.ratePerNight}</td>
                <td class="${status.className}">
                    ${status.text}
                </td>
            </tr>
        `;


        if (dashboardTable) {
            dashboardTable.innerHTML += dashboardRow;
        }

        if (roomsPageTable) {
            roomsPageTable.innerHTML += roomRow;
        }

    });
}


/* =========================
   ROOM SEARCH
========================= */

function searchRooms() {

    const searchInput =
        document.getElementById("searchInput");


    const searchText =
        searchInput.value
            .toLowerCase()
            .trim();


    const filteredRooms =
        rooms.filter(room =>

            room.roomNumber
                .toLowerCase()
                .includes(searchText)

            ||

            room.type
                .toLowerCase()
                .includes(searchText)
        );


    displayRooms(filteredRooms);
}


/* =========================
   SHOW ALL ROOMS
========================= */

function loadRooms() {

    displayRooms(rooms);

    document.getElementById("searchInput").value = "";
}


/* =========================
   DISPLAY GUESTS
========================= */

function displayGuests() {

    const table =
        document.getElementById("guestTable");


    if (!table) {
        return;
    }


    table.innerHTML = "";


    guests.forEach(guest => {

        const row = `
            <tr>
                <td>${guest.guestId}</td>
                <td>${guest.name}</td>
                <td>${guest.phone}</td>
                <td>${guest.email || "-"}</td>
            </tr>
        `;


        table.innerHTML += row;

    });
}


/* =========================
   DISPLAY BOOKINGS
========================= */

function displayBookings() {

    const table =
        document.getElementById("bookingTable");


    if (!table) {
        return;
    }


    table.innerHTML = "";


    bookings.forEach(booking => {

        let statusClass = "";


        if (booking.status === "CONFIRMED") {
            statusClass = "status-confirmed";
        }


        if (booking.status === "CANCELLED") {
            statusClass = "status-cancelled";
        }


        const row = `
            <tr>
                <td>${booking.bookingId}</td>

                <td>
                    ${booking.room?.roomNumber || "-"}
                </td>

                <td>
                    ${booking.guest?.name || "-"}
                </td>

                <td>${booking.checkIn}</td>

                <td>${booking.checkOut}</td>

                <td>₹${booking.totalCost}</td>

                <td class="${statusClass}">
                    ${booking.status}
                </td>
            </tr>
        `;


        table.innerHTML += row;

    });
}


/* =========================
   DASHBOARD BOOKINGS
========================= */

function displayDashboardBookings() {

    const table =
        document.getElementById(
            "dashboardBookingTable"
        );


    if (!table) {
        return;
    }


    table.innerHTML = "";


    bookings.forEach(booking => {

        let statusClass = "";


        if (booking.status === "CONFIRMED") {
            statusClass = "status-confirmed";
        }


        if (booking.status === "CANCELLED") {
            statusClass = "status-cancelled";
        }


        const row = `
            <tr>

                <td>${booking.bookingId}</td>

                <td>
                    ${booking.room?.roomNumber || "-"}
                </td>

                <td>
                    ${booking.guest?.name || "-"}
                </td>

                <td>${booking.checkIn}</td>

                <td>${booking.checkOut}</td>

                <td class="${statusClass}">
                    ${booking.status}
                </td>

            </tr>
        `;


        table.innerHTML += row;

    });
}

/* =========================
   GENERATE BOOKING REPORT
========================= */

function generateReport() {

    const fromValue =
        document.getElementById("reportFrom").value;

    const toValue =
        document.getElementById("reportTo").value;


    if (!fromValue || !toValue) {

        alert("Please select both dates.");

        return;
    }


    const fromDate =
        new Date(fromValue);

    const toDate =
        new Date(toValue);


    if (fromDate > toDate) {

        alert("From date must be before To date.");

        return;
    }


    /*
       The selected To Date is treated as
       part of the reporting period.
    */

    const endDate =
        new Date(toDate);

    endDate.setDate(
        endDate.getDate() + 1
    );


    /*
       A booking is included if its dates
       overlap the selected period.
    */

    const filteredBookings =
        bookings.filter(booking => {

            const checkIn =
                new Date(booking.checkIn);

            const checkOut =
                new Date(booking.checkOut);


            return (
                checkIn < endDate &&
                checkOut > fromDate
            );

        });


    /* Count bookings */

    const confirmedBookings =
        filteredBookings.filter(
            booking =>
                booking.status === "CONFIRMED"
        );


    const cancelledBookings =
        filteredBookings.filter(
            booking =>
                booking.status === "CANCELLED"
        );


    /* Update summary */

    document.getElementById(
        "reportTotalRooms"
    ).textContent = rooms.length;


    document.getElementById(
        "reportTotalBookings"
    ).textContent =
        filteredBookings.length;


    document.getElementById(
        "reportConfirmed"
    ).textContent =
        confirmedBookings.length;


    document.getElementById(
        "reportCancelled"
    ).textContent =
        cancelledBookings.length;


    /* Display bookings */

    const table =
        document.getElementById(
            "reportBookingTable"
        );


    table.innerHTML = "";


    if (filteredBookings.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="6">
                    No bookings found for the selected period.
                </td>
            </tr>
        `;

        return;
    }


    filteredBookings.forEach(booking => {

        let statusClass = "";


        if (booking.status === "CONFIRMED") {
            statusClass = "status-confirmed";
        }


        if (booking.status === "CANCELLED") {
            statusClass = "status-cancelled";
        }


        const row = `
            <tr>

                <td>
                    ${booking.bookingId}
                </td>

                <td>
                    ${booking.room?.roomNumber || "-"}
                </td>

                <td>
                    ${booking.guest?.name || "-"}
                </td>

                <td>
                    ${booking.checkIn}
                </td>

                <td>
                    ${booking.checkOut}
                </td>

                <td class="${statusClass}">
                    ${booking.status}
                </td>

            </tr>
        `;


        table.innerHTML += row;

    });
}

loadData();