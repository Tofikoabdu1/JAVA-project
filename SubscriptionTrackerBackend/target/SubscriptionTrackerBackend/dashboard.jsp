<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

  <style>
    * {
      box-sizing: border-box;
      font-family: Inter, Segoe UI, Arial, sans-serif;
    }

    body {
      margin: 0;
      background: #020617;
      color: #e5e7eb;
    }

    /* NAVBAR */
    .navbar {
      position: sticky;
      top: 0;
      z-index: 100;
      background: rgba(2,6,23,.9);
      backdrop-filter: blur(10px);
      border-bottom: 1px solid rgba(255,255,255,.06);
    }

    .nav-inner {
      max-width: 1200px;
      margin: auto;
      padding: 14px 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .logo {
      font-weight: 700;
      color: #6ee7b7;
      font-size: 18px;
    }

    /* HERO SLIDER */
    .hero-slider {
      max-width: 1200px;
      margin: 32px auto;
      border-radius: 20px;
      overflow: hidden;
      position: relative;
      border: 1px solid rgba(255,255,255,.06);
    }

    .slides {
      position: relative;
      height: 320px;
    }

    .slide {
      position: absolute;
      inset: 0;
      opacity: 0;
      transition: opacity .8s ease;
      pointer-events: none;
    }

    .slide.active {
      opacity: 1;
      z-index: 1;
      pointer-events: auto;
    }

    .slide img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      filter: brightness(.55);
    }

    .slide-content {
      position: absolute;
      inset: 0;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding: 40px;
      max-width: 600px;
    }

    /* DASHBOARD CONTENT (UNCHANGED STYLES) */
    .container {
      max-width: 1200px;
      margin: auto;
      padding: 24px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }
     :root {
        --bg: #0f172a;
        --card: #0b1220;
        --muted: #94a3b8;
        --accent: #6ee7b7;
        --danger: #fb7185;
      }

      * {
        box-sizing: border-box;
        font-family: Inter, Segoe UI, Arial, sans-serif;
      }

      body {
        margin: 0;
        background: linear-gradient(180deg, #020617, #020617);
        color: #e5e7eb;
      }

      /* ================= NAVBAR ================= */
      .navbar {
        position: sticky;
        top: 0;
        z-index: 100;
        background: rgba(2, 6, 23, 0.9);
        backdrop-filter: blur(10px);
        border-bottom: 1px solid rgba(255, 255, 255, 0.06);
      }

      .nav-inner {
        max-width: 1200px;
        margin: auto;
        padding: 14px 24px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .logo {
        font-weight: 700;
        font-size: 18px;
        color: var(--accent);
      }

      /* ================= HERO SLIDER ================= */
      .hero-slider {
        max-width: 1200px;
        margin: 40px auto 20px;
        border-radius: 20px;
        overflow: hidden;
        position: relative;
        border: 1px solid rgba(255, 255, 255, 0.06);
      }

      .slides {
        position: relative;
        height: 320px;
      }

      .slide {
        position: absolute;
        inset: 0;
        opacity: 0;
        transition: opacity 0.8s ease;
      }

      .slide.active {
        opacity: 1;
        z-index: 1;
      }

      .slide img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        filter: brightness(0.55);
      }

      .slide-content {
        position: absolute;
        inset: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 40px;
        max-width: 600px;
      }

      .slide-content h1 {
        font-size: 34px;
        margin: 0 0 12px;
      }

      .slide-content p {
        color: var(--muted);
        font-size: 16px;
        line-height: 1.6;
      }

      /* ================= DASHBOARD ================= */
      .container {
        max-width: 1200px;
        margin: 30px auto;
        padding: 0 24px 40px;
      }

      .stats {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 16px;
        margin-bottom: 24px;
      }

      .stat-card {
        background: rgba(255, 255, 255, 0.03);
        border: 1px solid rgba(255, 255, 255, 0.05);
        border-radius: 14px;
        padding: 18px;
      }

      .card {
        background: rgba(255, 255, 255, 0.03);
        border: 1px solid rgba(255, 255, 255, 0.05);
        border-radius: 16px;
        padding: 20px;
        margin-bottom: 22px;
      }

      form.add {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
      }

      input {
        padding: 10px 12px;
        border-radius: 10px;
        border: 1px solid rgba(255, 255, 255, 0.08);
        background: transparent;
        color: inherit;
      }

      .btn {
        background: linear-gradient(90deg, var(--accent), #4ade80);
        border: none;
        padding: 10px 16px;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
        color: #052018;
      }

      .btn.ghost {
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.12);
        color: var(--accent);
      }

      table {
        width: 100%;
        border-collapse: collapse;
      }

      thead th {
        padding: 14px 10px;
        font-size: 13px;
        color: var(--muted);
        text-align: left;
      }

      tbody td {
        padding: 14px 10px;
        border-top: 1px solid rgba(255, 255, 255, 0.05);
      }

      .empty {
        text-align: center;
        padding: 20px;
        color: var(--muted);
      }

      @media (max-width: 768px) {
        .slides {
          height: 260px;
        }
        .slide-content h1 {
          font-size: 26px;
        }
      }

  </style>
</head>

<body>

<!-- NAVBAR -->
<div class="navbar">
  <div class="nav-inner">
    <div class="logo">SubTrack</div>
   <button id="logoutBtn" class="btn ghost">Logout</button>
  </div>
</div>

<!-- HERO SLIDER -->
<div class="hero-slider">
  <div class="slides">
    <div class="slide active">
      <img src="https://images.unsplash.com/photo-1593182440959-9d5165b29b59">
      <div class="slide-content">
        <h1>Manage Subscriptions</h1>
        <p>Track all your recurring payments.</p>
      </div>
    </div>

    <div class="slide">
      <img src="https://images.unsplash.com/photo-1556740714-a8395b3bf30f">
      <div class="slide-content">
        <h1>Never Miss Due Dates</h1>
        <p>Stay in control of your expenses.</p>
      </div>
    </div>

    <div class="slide">
      <img src="https://images.unsplash.com/photo-1522202176988-66273c2fd55f">
      <div class="slide-content">
        <h1>Clean & Simple Dashboard</h1>
        <p>Designed for clarity and speed.</p>
      </div>
    </div>
  </div>
</div>

<!-- YOUR EXISTING DASHBOARD HTML GOES HERE (UNCHANGED) -->
<div class="container">
  <!-- table, forms, modals, stats — unchanged -->
   <div class="stats">
        <div class="stat-card">
          <span>Total Subscriptions</span>
          <h3 id="totalSubs">0</h3>
        </div>
        <div class="stat-card">
          <span>Monthly Spend</span>
          <h3 id="monthlySpend">0.00</h3>
        </div>
        <div class="stat-card">
          <span>Upcoming Due</span>
          <h3 id="upcomingDue">—</h3>
        </div>
      </div>

      <div class="card">
        <h3>Add Subscription</h3>
        <form id="addForm" class="add">
          <input id="addName" placeholder="Name" required />
          <input id="addAmount" type="number" step="0.01" placeholder="Amount" required />
          <input id="addDue" type="number" min="1" max="31" placeholder="Date" required />
          <button class="btn">Add</button>
        </form>
      </div>

      <div class="card">
        <h3>Your Subscriptions</h3>
        <table id="subsTable">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Amount</th>
              <th>Due</th>
              <th>Next</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody></tbody>
        </table>
      </div>
    </div>

    <!-- EDIT MODAL -->
    <div
      id="editModal"
      style="
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0, 0, 0, 0.6);
        align-items: center;
        justify-content: center;
      "
    >
      <div
        style="
          background: var(--card);
          padding: 20px;
          border-radius: 12px;
          width: 360px;
          max-width: 92%;
          margin: auto;
        "
      >
        <h3 style="margin-top: 0">Edit Subscription</h3>
        <div style="display: flex; flex-direction: column; gap: 8px">
          <input
            id="editName"
            placeholder="Name"
            style="padding: 8px; border-radius: 8px"
          />
          <input
            id="editAmount"
            type="number"
            step="0.01"
            placeholder="Amount"
            style="padding: 8px; border-radius: 8px"
          />
          <input
            id="editDue"
            type="number"
            min="1"
            max="31"
            placeholder="Due day"
            style="padding: 8px; border-radius: 8px"
          />
        </div>
        <div
          style="
            display: flex;
            justify-content: flex-end;
            gap: 8px;
            margin-top: 12px;
          "
        >
          <button id="cancelEdit" class="btn ghost">Cancel</button>
          <button id="saveEdit" class="btn">Save</button>
        </div>
      </div>

</div>

<script>
  const ctx = "<%= request.getContextPath() %>";
  let editId = null;

  /* =====================================================
     ✅ HERO SLIDER FIX — ONLY THIS PART IS NEW
     ===================================================== */
  function initHeroSlider() {
    const slides = document.querySelectorAll(".slide");
    if (!slides || slides.length <= 1) return;

    let current = 0;

    setInterval(() => {
      slides[current].classList.remove("active");
      current = (current + 1) % slides.length;
      slides[current].classList.add("active");
    }, 3000);
  }

  /* =====================================================
     ✅ PAGE INIT — DOES NOT TOUCH YOUR LOGIC
     ===================================================== */
  function initPage() {
    initHeroSlider();   // <-- ONLY addition
    fetchSubs();        // your original function
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initPage);
  } else {
    initPage();
  }

  /* =====================================================
     BELOW THIS LINE:
     fetchSubs()
     add/edit/delete
     modal handling
     date formatting
     logout
     ===================================================== */
     function escapeHtml(str) {
        if (!str) return "";
        return String(str).replace(
          /[&<>\"']/g,
          (c) =>
            ({
              "&": "&amp;",
              "<": "&lt;",
              ">": "&gt;",
              '"': "&quot;",
              "'": "&#39;",
            }[c])
        );
      }

      function showEditModal() {
        document.getElementById("editModal").style.display = "flex";
      }
      function hideEditModal() {
        document.getElementById("editModal").style.display = "none";
        editId = null;
      }

      async function fetchSubs() {
        const res = await fetch(ctx + "/api/subscriptions", {
          credentials: "same-origin",
        });
        if (!res.ok) {
          if (res.status === 401) return (window.location = ctx + "/login.jsp");
          document.querySelector("#listArea").innerHTML =
            '<div class="empty">Failed to load subscriptions.</div>';
          return;
        }
        const body = await res.json().catch(() => ({}));
        const subs = body && body.data ? body.data : [];
        // update dashboard stats
        try {
          const total = subs.length;
          const monthly = subs.reduce(
            (acc, s) => acc + (Number(s.amount) || 0),
            0
          );
          const now = Date.now();
          const dates = subs
            .map((s) =>
              s.nextDueDate ? new Date(Number(s.nextDueDate)) : null
            )
            .filter(Boolean);
          let upcoming = null;
          if (dates.length > 0) {
            // prefer next due dates in the future; otherwise pick the nearest
            const future = dates.filter((d) => d.getTime() >= now);
            const candidates = future.length > 0 ? future : dates;
            upcoming = candidates.reduce((a, b) =>
              a.getTime() < b.getTime() ? a : b
            );
          }
          const totalEl = document.getElementById("totalSubs");
          const monthlyEl = document.getElementById("monthlySpend");
          const upcomingEl = document.getElementById("upcomingDue");
          if (totalEl) totalEl.innerText = String(total);
          if (monthlyEl) monthlyEl.innerText = monthly.toFixed(2);
          if (upcomingEl)
            upcomingEl.innerText = upcoming
              ? upcoming.toLocaleDateString()
              : "—";
        } catch (e) {
          // ignore stat calc errors
          console.warn("Stats calc error", e);
        }
        const tbody = document.querySelector("#subsTable tbody");
        tbody.innerHTML = "";
        if (subs.length === 0) {
          tbody.innerHTML =
            '<tr><td class="empty" colspan="6">No subscriptions yet.</td></tr>';
          return;
        }
        subs.forEach((s) => {
          const tr = document.createElement("tr");
          const nextDue = s.nextDueDate
            ? new Date(s.nextDueDate).toLocaleDateString()
            : "";
          tr.innerHTML =
            "<td>" +
            s.id +
            "</td>" +
            '<td class="name">' +
            escapeHtml(s.name) +
            "</td>" +
            '<td class="amount">' +
            s.amount +
            "</td>" +
            "<td>" +
            s.dueDay +
            "</td>" +
            "<td>" +
            nextDue +
            "</td>" +
            "<td>" +
            '<button data-id="' +
            s.id +
            '" class="btn editBtn">Edit</button> ' +
            '<button data-id="' +
            s.id +
            '" class="btn ghost delBtn">Delete</button> ' +
            '<a class="btn ghost" href="' +
            ctx +
            "/markPaid.jsp?id=" +
            s.id +
            '">Mark Paid</a>' +
            "</td>";
          tbody.appendChild(tr);
        });

        // attach handlers
        document.querySelectorAll(".editBtn").forEach((b) => {
          b.addEventListener("click", async (e) => {
            editId = e.currentTarget.getAttribute("data-id");
            const subsRes = await fetch(ctx + "/api/subscriptions", {
              credentials: "same-origin",
            });
            const subsBody = await subsRes.json().catch(() => ({}));
            const subs = subsBody && subsBody.data ? subsBody.data : [];
            const s = subs.find((x) => String(x.id) === String(editId));
            if (!s) return alert("Subscription not found");
            document.getElementById("editName").value = s.name || "";
            document.getElementById("editAmount").value = s.amount || "";
            document.getElementById("editDue").value = s.dueDay || "";
            showEditModal();
          });
        });

        document.querySelectorAll(".delBtn").forEach((b) => {
          b.addEventListener("click", async (e) => {
            const id = e.currentTarget.getAttribute("data-id");
            if (!confirm("Delete subscription #" + id + "?")) return;
            const res = await fetch(ctx + "/api/subscriptions/" + id, {
              method: "DELETE",
              credentials: "same-origin",
            });
            if (res.ok) {
              await fetchSubs();
            } else if (res.status === 401) {
              window.location = ctx + "/login.jsp";
            } else {
              const b = await res.json().catch(() => ({}));
              alert(b.message || "Failed to delete");
            }
          });
        });
      }

      // (add/edit handlers attached safely later)

      // (removed unguarded logout handler above) - guarded attachment below

      // Attach modal handlers and initialize slider + data when DOM is ready
      function attachModalHandlers() {
        const cancelBtn = document.getElementById("cancelEdit");
        if (cancelBtn) {
          cancelBtn.addEventListener("click", (e) => {
            e.preventDefault();
            hideEditModal();
          });
        }

        const saveBtn = document.getElementById("saveEdit");
        if (saveBtn) {
          saveBtn.addEventListener("click", async (e) => {
            e.preventDefault();
            if (!editId) return alert("No subscription selected");
            const name = document.getElementById("editName").value.trim();
            const amount = parseFloat(
              document.getElementById("editAmount").value
            );
            const due_day = parseInt(
              document.getElementById("editDue").value,
              10
            );
            const res = await fetch(ctx + "/api/subscriptions/" + editId, {
              method: "PUT",
              credentials: "same-origin",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ name, amount, due_day }),
            });
            if (res.ok) {
              hideEditModal();
              await fetchSubs();
            } else if (res.status === 401) {
              window.location = ctx + "/login.jsp";
            } else {
              const b = await res.json().catch(() => ({}));
              alert(b.message || "Failed to save");
            }
          });
        }
      }
 function initPage() {
        initHeroSlider();
        attachModalHandlers();
        fetchSubs();
      }

      if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initPage);
      } else {
        initPage();
      }

      // safe event attachments (guards)
      const logoutBtn = document.getElementById("logoutBtn");
      if (logoutBtn) {
        logoutBtn.addEventListener("click", async () => {
          await fetch(ctx + "/api/auth/logout", {
            method: "POST",
            credentials: "same-origin",
          }).catch(() => {});
          window.location = ctx + "/login.jsp";
        });
      }

      const addFormEl = document.getElementById("addForm");
      if (addFormEl) {
        addFormEl.addEventListener("submit", async (e) => {
          e.preventDefault();
          const name = document.getElementById("addName").value.trim();
          const amount = parseFloat(document.getElementById("addAmount").value);
          const due_day = parseInt(document.getElementById("addDue").value, 10);
          const res = await fetch(ctx + "/api/subscriptions", {
            method: "POST",
            credentials: "same-origin",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, amount, due_day }),
          });
          if (res.ok) {
            addFormEl.reset();
            await fetchSubs();
          } else if (res.status === 401) {
            window.location = ctx + "/login.jsp";
          } else {
            const b = await res.json().catch(() => ({}));
            alert(b.message || "Failed to add subscription");
          }
        });
      }

      // (init already performed above)

</script>

</body>
</html>
