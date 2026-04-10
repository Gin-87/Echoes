import React, { useState, useEffect } from "react";
import { Box, useMediaQuery } from "@mui/material";
import CarouselComponent from "../Atoms/CarouselComponent.jsx";
import CardComponent from "../Atoms/CardComponent.jsx";
import { useToast } from "../../General/Business/ToastContext.jsx";
import request from '../../../../utils/request';

function HomePageDisplay() {
  const { showToast } = useToast();
  const [characters, setCharacters] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  // 根据屏幕宽度动态调整总列数
  const is2K = useMediaQuery("(min-width: 2048px)");
  const is1440p = useMediaQuery("(min-width: 1440px)");
  const is1080p = useMediaQuery("(min-width: 1080px)");

  // 一行总共分几列（包括轮播和卡片）
  const totalColumns = is2K ? 6 : is1440p ? 5 : is1080p ? 4 : 3;

  // 轮播图数据
  const carouselItems = [
    {
      image:
        "https://firebasestorage.googleapis.com/v0/b/xchatsair/o/ugc%2FAgACAgUAAxkBAAEZy0pnZUDp7Ynd-G_t22svBEBRJ6QcRwACTsYxG8rkKFcMWbPfGTj8AgEAAwIAA3kAAzYE.jpg?alt=media&token=f7ba5c7d-cd38-4bfe-b57e-5e3d7da0924e",
      alt: "Cat 1",
    },
    {
      image:
        "https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/3c2bb151-34d7-454f-9f58-ca736aeb851e/original=true,quality=90/35658176.jpeg",
      alt: "Cat 2",
    },
    {
      image:
        "https://firebasestorage.googleapis.com/v0/b/xchatsair/o/ugc%2FAgACAgUAAxkBAAEZyyVnZT1goY1JwZcYQ1WkA3dVRkLmYQACSMYxG8rkKFcMEhe_MdKdcwEAAwIAA3kAAzYE.jpg?alt=media&token=d1b52bac-1d4b-4a4a-9524-04b0f36bc5db",
      alt: "Cat 3",
    },
  ];

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

  // 加载角色数据
  const loadCharacters = async () => {
    setIsLoading(true);
    try {
      const data = await request.get('/api/characters/getAll');
      if (data && data.code === 200) {
        setCharacters(sortCharacters(data.data));
      }
    } catch (error) {
      console.error('Failed to load characters:', error);
    } finally {
      setIsLoading(false);
    }
  };

  // 只在组件挂载时加载一次数据
  useEffect(() => {
    loadCharacters();
  }, []); // 空依赖数组，只在组件挂载时执行

  // 监听登录状态变化和收藏操作
  useEffect(() => {
    const handleStatusChange = () => {
      loadCharacters();
    };

    window.addEventListener('loginSuccess', handleStatusChange);
    window.addEventListener('logout', handleStatusChange);

    return () => {
      window.removeEventListener('loginSuccess', handleStatusChange);
      window.removeEventListener('logout', handleStatusChange);
    };
  }, []);

  // 添加收藏状态更新函数
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

  // 监听搜索结果和重新加载事件
  useEffect(() => {
    const handleSearchResults = (event) => {
      // 对搜索结果也应用排序
      setCharacters(sortCharacters(event.detail));
      setIsLoading(false);
    };

    const handleReload = () => {
      loadCharacters();
    };

    window.addEventListener('searchResults', handleSearchResults);
    window.addEventListener('reloadCharacters', handleReload);

    return () => {
      window.removeEventListener('searchResults', handleSearchResults);
      window.removeEventListener('reloadCharacters', handleReload);
    };
  }, []);

  return (
    <Box
      sx={{
        width: {
          lg: "80%",
          xs: "90%"
        },
        margin: "0 auto",
        padding: "20px 0",
      }}
    >
      <Box
        sx={{
          display: "grid",
          gridTemplateColumns: `repeat(${totalColumns}, 1fr)`,
          gap: "32px",
          gridAutoRows: "auto",
        }}
      >
        {/* 左侧：轮播，占用 2 列 */}
        <Box
          sx={{
            gridColumn: "span 2",
            position: "relative",
            overflow: "hidden",
            height: "500px",
          }}
        >
          <Box
            sx={{
              position: "absolute",
              inset: 0,
              height: "100%",
            }}
          >
            <CarouselComponent items={carouselItems} />
          </Box>
        </Box>

        {/* 右侧：卡片列表或无结果提示 */}
        {characters.length > 0 ? (
          characters.map((character) => (
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
          ))
        ) : (
          <Box sx={{ gridColumn: '3/-1', textAlign: 'center', py: 4 }}>
            暂无搜索结果
          </Box>
        )}
      </Box>
    </Box>
  );
}

export default HomePageDisplay;