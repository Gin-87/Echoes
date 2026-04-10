import React, { useState } from "react";
import { Box, TextField, Button, InputAdornment } from "@mui/material";
import { useToast } from "../../General/Business/ToastContext";
import { useTranslation } from "react-i18next";
import SearchIcon from '@mui/icons-material/Search';

function SearchBar({ onSearch }) {
  const { showToast } = useToast();
  const { t } = useTranslation();
  const [keyword, setKeyword] = useState('');

  const handleSearch = async () => {
    if (keyword.trim()) {
      onSearch(keyword.trim());
    } else {
      onSearch('');
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        gap: "10px",
        width: {
          xs: "100%",
          sm: "80%",
          md: "60%",
        },
        maxWidth: "800px",
        minWidth: "620px",
      }}
    >
      <TextField
        fullWidth
        variant="outlined"
        placeholder={t("searchPlaceholder")}
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        onKeyPress={handleKeyPress}
        sx={{
          backgroundColor: "#fff",
          borderRadius: "4px",
          "& .MuiInputBase-root": {
            height: "40px",
          },
        }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon sx={{ color: 'text.secondary' }} />
            </InputAdornment>
          ),
        }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={handleSearch}
        sx={{
          height: "40px",
        }}
      >
        {t("searchButtonPlaceHolder")}
      </Button>
    </Box>
  );
}

export default SearchBar;
