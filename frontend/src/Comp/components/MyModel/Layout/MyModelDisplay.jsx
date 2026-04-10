import React, { useState, useEffect } from "react";
import { Box, useMediaQuery, CircularProgress } from "@mui/material";
import CardComponent from "../../HomePage/Atoms/CardComponent.jsx";
import { useToast } from "../../General/Business/ToastContext.jsx";
import { useNavigate } from "react-router-dom";
import request from '../../../../utils/request';

function MyModelDisplay() {
  const { showToast } = useToast();
  const [characters, setCharacters] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  // 根据屏幕宽度动态调整总列数
  const is2K = useMediaQuery("(min-width: 2048px)");
  const is1440p = useMediaQuery("(min-width: 1440px)");
  const is1080p = useMediaQuery("(min-width: 1080px)");

  // 一行总共分几列
  const totalColumns = is2K ? 6 : is1440p ? 4 : is1080p ? 4 : 3;

  // 添加排序函数
  const sortCharacters = (chars) => {
    return [...chars].sort((a, b) => {
      // 首先按照收藏数排序
      if (b.num_of_favorites !== a.num_of_favorites) {
        return b.num_of_favorites - a.num_of_favorites;
      }
      // 如果收藏数相同，按照创建时间排序
      return new Date(a.created_at) - new Date(b.created_at);
    });
  };

  // 修改加载数据的函数
  const loadCharacters = async () => {
    setIsLoading(true);
    try {
      const data = await request.get('/api/characters/getMy');
      if (data && data.code === 200) {
        setCharacters(sortCharacters(data.data));
      } else if (data) {
        showToast(data.message || "加载失败", "error");
      }
    } catch (error) {
      console.error('Failed to load characters:', error);
      showToast("加载失败", "error");
    } finally {
      setIsLoading(false);
    }
  };

  // 修改删除后的处理函数
  const handleDeleteSuccess = () => {
    loadCharacters();  // 重新加载数据时会自动排序
  };

  // 收藏状态更新函数
  const handleFavoriteUpdate = (characterId, newFavoriteStatus, newFavoriteCount) => {
    setCharacters(prevCharacters =>
      prevCharacters.map(character =>
        character.id === characterId
          ? {
            ...character,
            isFavorite: newFavoriteStatus,
            num_of_favorites: newFavoriteCount
          }
          : character
      )
    );
  };

  // 组件挂载和登录状态变化时加载数据
  useEffect(() => {
    loadCharacters();

    // 监听登录状态变化
    const handleLoginStatusChange = () => {
      loadCharacters();
    };

    window.addEventListener('loginSuccess', handleLoginStatusChange);
    window.addEventListener('logout', handleLoginStatusChange);

    return () => {
      window.removeEventListener('loginSuccess', handleLoginStatusChange);
      window.removeEventListener('logout', handleLoginStatusChange);
    };
  }, []);

  return (
    <Box
      sx={{
        width: "80%",
        margin: "0 auto",
        padding: "20px 0",
      }}
    >
      {isLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: `repeat(${totalColumns}, 1fr)`,
            gap: "32px",
            gridAutoRows: "auto",
          }}
        >
          {characters.map((character) => (
            <CardComponent
              key={character.id}
              id={character.id}
              title={character.name}
              description={character.description}
              image={character.avatar_url}
              creatorNickname={character.owner_name}
              isFavorited={character.isFavorite}
              currentFavorited={character.num_of_favorites}
              onFavoriteUpdate={handleFavoriteUpdate}
            />
          ))}
        </Box>
      )}
    </Box>
  );
}

export default MyModelDisplay; 