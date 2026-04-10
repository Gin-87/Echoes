import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./views/App.jsx";
import "./utils/i18n/i18n.js"; // 保留多语言初始化
import { BrowserRouter } from "react-router-dom"; // 顶层引入 BrowserRouter
import { ToastProvider } from "./Comp/components/General/Business/ToastContext.jsx"; // 引入 ToastProvider

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);

root.render(
  <StrictMode>
    <ToastProvider> {/* 包裹 ToastProvider */}
      <BrowserRouter>
        <App /> {/* App 组件内无需再次嵌套 Router */}
      </BrowserRouter>
    </ToastProvider>
  </StrictMode>
);
