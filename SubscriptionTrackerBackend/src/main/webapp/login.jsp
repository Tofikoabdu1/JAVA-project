<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Login — Subscription Tracker</title>

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
        min-height: 100vh;
        background: linear-gradient(180deg, #020617, #020617);
        color: #e6eef8;
      }

      /* ============ LAYOUT ============ */
      .auth-layout {
        display: grid;
        grid-template-columns: 1.2fr 1fr;
        min-height: 100vh;
      }

      /* ============ HERO ============ */
      .auth-hero {
        padding: 60px;
        background: linear-gradient(
          135deg,
          rgba(110, 231, 183, 0.18),
          rgba(2, 6, 23, 0.9)
        );
        display: flex;
        flex-direction: column;
        justify-content: center;
      }

      .auth-hero h1 {
        font-size: 38px;
        margin-bottom: 16px;
      }

      .auth-hero p {
        color: var(--muted);
        line-height: 1.6;
        max-width: 480px;
      }

      /* ============ CARD ============ */
      .auth-panel {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 32px;
      }

      .card {
        width: 100%;
        max-width: 420px;
        background: rgba(255, 255, 255, 0.03);
        border: 1px solid rgba(255, 255, 255, 0.06);
        padding: 30px;
        border-radius: 18px;
        box-shadow: 0 20px 40px rgba(2, 6, 23, 0.7);
      }

      .brand {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 18px;
      }

      .logo {
        width: 44px;
        height: 44px;
        border-radius: 12px;
        background: linear-gradient(90deg, var(--accent), #4ade80);
      }

      h2 {
        margin: 0;
        font-size: 22px;
      }

      .lead {
        margin: 6px 0 18px;
        color: var(--muted);
        font-size: 14px;
      }

      /* ============ FORM ============ */
      label {
        display: block;
        font-size: 13px;
        margin-bottom: 6px;
        color: var(--muted);
      }

      input[type="text"],
      input[type="password"] {
        width: 100%;
        padding: 12px 14px;
        border-radius: 10px;
        border: 1px solid rgba(255, 255, 255, 0.08);
        background: transparent;
        color: inherit;
        margin-bottom: 14px;
      }

      input:focus {
        outline: none;
        border-color: var(--accent);
      }

      .btn {
        background: linear-gradient(90deg, var(--accent), #4ade80);
        border: none;
        padding: 12px;
        border-radius: 12px;
        color: #052018;
        font-weight: 700;
        cursor: pointer;
        width: 100%;
        margin-top: 6px;
      }

      .muted {
        text-align: center;
        margin-top: 14px;
        color: var(--muted);
        font-size: 13px;
      }

      .link {
        color: var(--accent);
        text-decoration: none;
        font-weight: 600;
      }

      .error {
        background: rgba(255, 0, 0, 0.12);
        color: #fecaca;
        padding: 10px;
        border-radius: 10px;
        margin-bottom: 14px;
        border: 1px solid rgba(255, 0, 0, 0.2);
        font-size: 14px;
      }

      .toggle {
        position: relative;
        right: 38px;
        top: -42px;
        cursor: pointer;
        color: var(--muted);
        font-size: 12px;
        float: right;
      }

      @media (max-width: 900px) {
        .auth-layout {
          grid-template-columns: 1fr;
        }
        .auth-hero {
          display: none;
        }
      }
    </style>
  </head>

  <body>
    <div class="auth-layout">
      <!-- HERO -->
      <div class="auth-hero">
        <h1>Welcome Back 👋</h1>
        <p>
          Log in to track your subscriptions, control your monthly spending, and
          never miss a due date again.
        </p>
      </div>

      <!-- LOGIN PANEL -->
      <div class="auth-panel">
        <div class="card">
          <div class="brand">
            <div class="logo"></div>
            <div>
              <h2>Subscription Tracker</h2>
              <div class="lead">Sign in to your account</div>
            </div>
          </div>

          <% if (request.getAttribute("error") != null) { %>
          <div class="error"><%= request.getAttribute("error") %></div>
          <% } %>

          <form
            id="loginForm"
            method="post"
            action="<%= request.getContextPath() %>/api/auth/login"
          >
            <label for="emailOrUsername">Email or Username</label>
            <input
              type="text"
              id="emailOrUsername"
              name="emailOrUsername"
              required
              autocomplete="username"
            />

            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              required
              autocomplete="current-password"
            />

            <button type="submit" class="btn">Sign in</button>

            <div class="muted">
              Don’t have an account?
              <a
                class="link"
                href="<%= request.getContextPath() %>/register.jsp"
                >Create one</a
              >
            </div>
          </form>
        </div>
      </div>
    </div>

    <script>
      // password toggle (UNCHANGED LOGIC)
      (function () {
        const pwd = document.getElementById("password");
        if (!pwd) return;
        const btn = document.createElement("span");
        btn.className = "toggle";
        btn.textContent = "Show";
        btn.addEventListener("click", () => {
          if (pwd.type === "password") {
            pwd.type = "text";
            btn.textContent = "Hide";
          } else {
            pwd.type = "password";
            btn.textContent = "Show";
          }
        });
        pwd.parentNode.insertBefore(btn, pwd.nextSibling);
      })();
    </script>
  </body>
</html>
