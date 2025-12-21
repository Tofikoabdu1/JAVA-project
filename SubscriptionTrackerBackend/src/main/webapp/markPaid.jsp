<%@ page contentType="text/html;charset=UTF-8" language="java" %> <% String id =
request.getParameter("id"); %>
<!DOCTYPE html>
<html>
  <head>
    <title>Mark As Paid — Subscription Tracker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --bg: #020617;
        --card: #071827;
        --accent: #6ee7b7;
        --muted: #94a3b8;
      }

      * {
        box-sizing: border-box;
        font-family: Inter, Segoe UI, Arial, sans-serif;
      }

      body {
        margin: 0;
        background: linear-gradient(180deg, #020617, #020617);
        color: #e6eef8;
        min-height: 100vh;
      }

      /* ============ NAVBAR ============ */
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

      /* ============ HERO ============ */
      .hero {
        max-width: 1200px;
        margin: 40px auto 20px;
        padding: 32px;
        background: linear-gradient(
          135deg,
          rgba(110, 231, 183, 0.15),
          rgba(2, 6, 23, 0.9)
        );
        border-radius: 18px;
        border: 1px solid rgba(255, 255, 255, 0.06);
      }

      .hero h1 {
        margin: 0 0 8px;
        font-size: 28px;
      }

      .hero p {
        color: var(--muted);
        margin: 0;
      }

      /* ============ CARD ============ */
      .container {
        max-width: 800px;
        margin: 0 auto 60px;
        padding: 0 24px;
      }

      .card {
        background: rgba(255, 255, 255, 0.03);
        border: 1px solid rgba(255, 255, 255, 0.06);
        border-radius: 18px;
        padding: 26px;
      }

      .meta {
        color: var(--muted);
        margin-bottom: 12px;
        font-size: 14px;
      }

      .status {
        font-size: 16px;
        margin-bottom: 20px;
      }

      /* ============ BUTTONS ============ */
      .actions {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
      }

      .btn {
        background: linear-gradient(90deg, var(--accent), #4ade80);
        border: none;
        padding: 12px 18px;
        border-radius: 12px;
        color: #052018;
        font-weight: 700;
        cursor: pointer;
      }

      .btn.ghost {
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.12);
        color: var(--accent);
        text-decoration: none;
      }

      @media (max-width: 700px) {
        .hero h1 {
          font-size: 22px;
        }
      }
    </style>
  </head>

  <body>
    <!-- NAVBAR -->
    <div class="navbar">
      <div class="nav-inner">
        <div class="logo">SubTrack</div>
      </div>
    </div>

    <!-- HERO -->
    <div class="hero">
      <h1>Confirm Subscription Payment</h1>
      <p>Review the details below before marking this subscription as paid.</p>
    </div>

    <!-- CONTENT -->
    <div class="container">
      <div class="card">
        <div class="meta">Subscription ID: <strong><%= id %></strong></div>

        <div id="status" class="status">Loading subscription details…</div>

        <div class="actions">
          <button id="confirmBtn" class="btn">Confirm Mark Paid</button>
          <a
            class="btn ghost"
            href="<%= request.getContextPath() %>/dashboard.jsp"
          >
            Back to Dashboard
          </a>
        </div>
      </div>
    </div>

    <script>
      const ctx = "<%= request.getContextPath() %>";
      const id = '<%= id != null ? id.replace("'", "\\'") : "" %>';

      if (!id) {
        document.getElementById("status").innerText =
          "No subscription id provided.";
        document.getElementById("confirmBtn").disabled = true;
      }

      async function load() {
        const res = await fetch(ctx + "/api/subscriptions", {
          credentials: "same-origin",
        });
        if (!res.ok) {
          if (res.status === 401) window.location = ctx + "/login.jsp";
          document.getElementById("status").innerText =
            "Failed to load subscriptions.";
          return;
        }
        const body = await res.json();
        const subs = body && body.data ? body.data : [];
        const s = subs.find((x) => String(x.id) === String(id));
        if (!s) {
          document.getElementById("status").innerText =
            "Subscription not found or you do not have permission.";
          document.getElementById("confirmBtn").disabled = true;
          return;
        }
        document.getElementById("status").innerHTML =
          "<strong>" +
          escapeHtml(s.name) +
          "</strong> — " +
          new Date(s.nextDueDate).toLocaleDateString() +
          " (" +
          s.amount +
          ")";
      }

      function escapeHtml(str) {
        if (!str) return "";
        return str.replace(
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

      document
        .getElementById("confirmBtn")
        .addEventListener("click", async () => {
          if (!confirm("Mark this subscription as paid?")) return;
          const res = await fetch(ctx + "/api/paid/" + id, {
            method: "POST",
            credentials: "same-origin",
          });
          if (res.ok) {
            const b = await res.json().catch(() => ({}));
            alert(b.message || "Marked as paid");
            window.location = ctx + "/dashboard.jsp";
          } else if (res.status === 401) {
            window.location = ctx + "/login.jsp";
          } else {
            const b = await res.json().catch(() => ({}));
            alert(b.message || "Failed to mark paid");
          }
        });

      load();
    </script>
  </body>
</html>
