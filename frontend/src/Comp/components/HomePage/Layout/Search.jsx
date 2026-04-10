import React from "react";
import { Box } from "@mui/material";
import SearchBar from "../Atoms/SearchBar.jsx";
import CharacterFilter from "../Atoms/CharacterFilter.jsx";
import request from '../../../../utils/request';

function Search({ onSearch }) {
  const handleSearch = async (keyword) => {
    try {
      const url = keyword
        ? `/api/characters/search?keyword=${encodeURIComponent(keyword)}`
        : `/api/characters/getAll`;
      const data = await request.get(url);
      if (data && data.code === 200) {
        window.dispatchEvent(new CustomEvent('searchResults', { detail: data.data }));
      }
    } catch (error) {
      console.error('Search failed:', error);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: "20px",
        backgroundColor: "#FFFFFF",
        width: "100%",
        position: "relative",
      }}
    >
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          width: "60%",
        }}
      >
        <SearchBar onSearch={handleSearch} />
      </Box>

      <Box
        sx={{
          position: "absolute",
          right: "15%",
        }}
      >
        <CharacterFilter />
      </Box>
    </Box>
  );
}

export default Search;
