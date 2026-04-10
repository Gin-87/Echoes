import React, { createContext, useState, useContext } from "react";
import {Snackbar, Alert, duration} from "@mui/material";

// 创建上下文
const ToastContext = createContext();

// 提供 Toast 上下文的 Provider
export const ToastProvider = ({ children }) => {
  const [toast, setToast] = useState({
    open: false,
    message: "",
    severity: "info", // 可选值: "success", "error", "warning", "info"
    duration: 3000,
  });

  // 显示 Toast
  const showToast = (message, severity = "info") => {
    setToast({ open: true, message, severity,duration:3000 });
  };

  // 关闭 Toast
  const closeToast = () => {
    setToast((prev) => ({ ...prev, open: false }));
  };

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}

      {/* 全局 Toast */}
      <Snackbar
        open={toast.open}
        autoHideDuration={toast.duration} // 动态设置自动关闭时间
        onClose={closeToast}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert onClose={closeToast} severity={toast.severity} sx={{ width: "100%" }}>
          {toast.message}
        </Alert>
      </Snackbar>
    </ToastContext.Provider>
  );
};

// 创建自定义 Hook，方便调用
export const useToast = () => useContext(ToastContext);
