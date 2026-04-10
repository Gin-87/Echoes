import i18n from "i18next";
import { initReactI18next } from "react-i18next";

// 引入语言资源文件
import en from "./en.json";
import zh from "./zh.json";

i18n
  .use(initReactI18next) // 绑定 react-i18next
  .init({
    resources: {
      en: {
        translation: en, // 加载英文资源
      },
      zh: {
        translation: zh, // 加载中文资源
      },
    },
    lng: localStorage.getItem("language") || "zh", // 默认从 localStorage 读取语言
    fallbackLng: "en", // 备用语言
    interpolation: {
      escapeValue: false, // React 已经处理了 XSS
    },
  });

// 定义语言切换函数
export const changeLanguage = (lang) => {
  i18n.changeLanguage(lang); // 切换语言
  localStorage.setItem("language", lang); // 将选择的语言存储到 localStorage
};

export default i18n;
