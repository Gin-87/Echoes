import React, { useState } from "react";
import {
  Button,
  Menu,
  Box,
  ToggleButtonGroup,
  ToggleButton,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import {useToast} from "../../General/Business/ToastContext.jsx";

function CharacterFilter() {
  const { showToast } = useToast(); // 从 context 中解构出 showToast
  const [anchorEl, setAnchorEl] = useState(null);
  const [filters, setFilters] = useState({
    language: [],
    gender: [],
  });
  const [tempFilters, setTempFilters] = useState({
    language: [],
    gender: [],
  });

  const { t } = useTranslation();

  // 判断是否有任何已选的过滤
  const isAnyFilterSelected =
    filters.language.length > 0 || filters.gender.length > 0;

  const handleMenuClick = (event) => {
    setTempFilters(filters); // 打开菜单时将当前 filters 的值同步到 tempFilters
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  // 临时保存语言多选
  const handleTempLanguageChange = (event, newLanguages) => {
    setTempFilters((prev) => ({
      ...prev,
      language: newLanguages || [],
    }));
  };

  // 临时保存性别多选
  const handleTempGenderChange = (event, newGenders) => {
    setTempFilters((prev) => ({
      ...prev,
      gender: newGenders || [],
    }));
  };

  // 应用按钮点击
  const handleApplyFilters = () => {
    setFilters(tempFilters); // 将临时过滤条件保存到最终的 filters
    showToast("筛选条件已应用","success")
    handleMenuClose(); // 关闭菜单
  };





  return (
    <Box>
      {/* 更多筛选 按钮 */}
      <Button
        variant="outlined"
        size="small"
        onClick={handleMenuClick}
        sx={{
          textTransform: "none",
          color: isAnyFilterSelected ? "primary.main" : "text.primary",
          borderColor: isAnyFilterSelected ? "primary.main" : "text.secondary",
        }}
      >
        {t("filter")}
      </Button>

      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        transformOrigin={{ vertical: "top", horizontal: "left" }}
      >
        <Box sx={{ padding: 2, width: 300 }}>
          {/* 语言筛选 */}
          <Box sx={{ fontWeight: "bold", marginBottom: 1 }}>{t("language")}</Box>
          <ToggleButtonGroup
            value={tempFilters.language} // 使用临时状态
            onChange={handleTempLanguageChange}
            exclusive={false} // 多选模式
            sx={{
              display: "flex",
              gap: 1,
              "& .MuiToggleButton-root": {
                width: "80px",
                height: "30px",
              },
            }}
          >
            {[t("Chinese"), t("English")].map((lang) => (
              <ToggleButton
                key={lang}
                value={lang}
                sx={{
                  border: "1px solid rgba(0, 0, 0, 0.23) !important",
                  borderRadius: "6px !important",
                  "&.Mui-selected": {
                    backgroundColor: "blue",
                    color: "#fff",
                  },
                  "&.Mui-selected:hover": {
                    backgroundColor: "primary.dark",
                  },
                }}
              >
                {lang}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>

          {/* 性别筛选 */}
          <Box sx={{ fontWeight: "bold", margin: "16px 0 8px" }}>{t("gender")}</Box>
          <ToggleButtonGroup
            value={tempFilters.gender} // 使用临时状态
            onChange={handleTempGenderChange}
            exclusive={false}
            sx={{
              display: "flex",
              gap: 1,
              "& .MuiToggleButton-root": {
                width: "70px",
                height: "30px",
              },
            }}
          >
            {[t("male"), t("female"), t("unknown")].map((gender) => (
              <ToggleButton
                key={gender}
                value={gender}
                size="small"
                sx={{
                  border: "1px solid rgba(0, 0, 0, 0.23) !important",
                  borderRadius: "6px !important",
                  "&.Mui-selected": {
                    backgroundColor: "blue",
                    color: "#fff",
                  },
                  "&.Mui-selected:hover": {
                    backgroundColor: "primary.dark",
                  },
                }}
              >
                {gender}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>

          {/* 应用按钮 */}
          <Button
            variant="contained"
            color="primary"
            size="small"
            onClick={handleApplyFilters} // 点击应用保存当前的临时状态
            sx={{ marginTop: 2 }}
          >
            {t("applyButton")}
          </Button>
        </Box>
      </Menu>
    </Box>
  );
}

export default CharacterFilter;
