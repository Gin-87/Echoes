import React from "react";
import { Card, CardMedia, CardContent, Typography, Box } from "@mui/material";
import { useTranslation } from "react-i18next";
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { useToast } from "../../General/Business/ToastContext.jsx";
import request from '../../../../utils/request';
import { useNavigate } from "react-router-dom";

function CardComponent({ title, description, image, creatorNickname, isFavorited, currentFavorited, id, onFavoriteUpdate }) {
  const { t } = useTranslation();
  const { showToast } = useToast();
  const navigate = useNavigate();

  const handleFavoriteClick = async (e) => {
    e.stopPropagation();
    
    const token = localStorage.getItem('accessToken');
    if (!token) {
      window.dispatchEvent(new Event('openLoginDialog'));
      return;
    }

    const action = isFavorited ? 'remove_favorite' : 'add_favorite';
    try {
      const data = await request[isFavorited ? 'delete' : 'post'](
        `/api/user/favorites/${action}/${id}`
      );
      
      if (data && data.code === 200) {
        onFavoriteUpdate(id, !isFavorited, isFavorited ? currentFavorited - 1 : currentFavorited + 1);
        showToast(isFavorited ? "取消收藏成功" : "收藏成功", "success");
      }
    } catch (error) {
      console.error('Favorite operation failed:', error);
    }
  };

  // 添加卡片点击事件
  const handleCardClick = () => {
    window.open(`/character/${id}`, '_blank');
  };

  const handleWriteClick = () => {
    navigate(`/write/${id}`);
  };

  return (
    <Card
      sx={{
        borderRadius: "8px",
        height: "500px",
        cursor: 'pointer', // 添加鼠标指针样式
        '&:hover': {
          boxShadow: 6, // 添加悬停效果
        }
      }}
      onClick={handleCardClick}  // 添加点击事件
    >
      <CardMedia
        component="img"
        height="65%"
        image={image}
        alt={title}
        sx={{ borderRadius: "8px 8px 0 0" }}
      />

      <CardContent>
        <Typography variant="h6"
          noWrap
          sx={{
            borderRadius: "18px",
            height: "35px", // 设置固定高度
            overflow: "hidden",
            textOverflow: "ellipsis",
            display: "-webkit-box",
            WebkitLineClamp: 1, // 控制显示的行数
            WebkitBoxOrient: "vertical",
          }}>
          {title}
        </Typography>

        <Typography variant="body2" color="text.secondary"
          sx={{
            Height: "70px !important",
            maxHeight: "80px",
            minHeight: "80px",
            overflow: "hidden",
            textOverflow: "ellipsis",
            display: "block",
            borderBottom: "1px solid #e0e0e0", // 设置非常浅的灰色边框
            paddingBottom: "3px" // 设置底部内边距，使边框与文字有间隔
          }}>
          {description}
        </Typography>

        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            marginTop: "8px",
            maxWidth: "300px",
            overflow: "hidden",

          }}
        >
          <Typography variant="caption" noWrap color="text.secondary">
            {t("creator")} {creatorNickname}
          </Typography>
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              cursor: "pointer",
              marginLeft: "auto",
              '&:hover .MuiSvgIcon-root': {
                color: '#FF0000',
              },
            }}
            onClick={handleFavoriteClick}
          >
            {isFavorited ? (
              <FavoriteIcon fontSize="small" sx={{ color: '#FF0000' }} />
            ) : (
              <FavoriteBorderIcon fontSize="small" sx={{ color: 'gray' }} />
            )}
            <Typography variant="caption" sx={{ marginLeft: "4px", fontSize: "0.8rem", color: 'inherit' }}>
              {currentFavorited}
            </Typography>
          </Box>
        </Box>

      </CardContent>
    </Card>
  );
}

export default CardComponent;
