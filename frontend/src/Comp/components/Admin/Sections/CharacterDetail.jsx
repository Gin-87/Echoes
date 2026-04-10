import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import {
  Box,
  Typography,
  TextField,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormControl,
  FormLabel,
  Paper,
  Button,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import EditIcon from '@mui/icons-material/Edit';
import MailIcon from '@mui/icons-material/Mail';

// 复用标签组件，但移除 required 属性
const Label = ({ children }) => (
  <FormLabel
    sx={{
      mb: 1,
    }}
  >
    {children}
  </FormLabel>
);

const CharacterDetail = () => {
  const { id } = useParams();
  const { t } = useTranslation();
  const [character, setCharacter] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCharacter = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem('accessToken');
        const headers = token ? { 'Authorization': `Bearer ${token}` } : {};

        const response = await fetch(`/api/characters/getDetail/${id}`, {
          headers: headers
        });

        const data = await response.json();

        if (data.code === 200) {
          // 将后端返回的数据映射到组件状态
          const characterData = {
            id: id,
            code: data.data.code,
            name: data.data.name,
            avatar: data.data.avatar_url,
            background: data.data.backgroundStory,
            description: data.data.description,
            personalityTraits: data.data.personalityTraits,
            languageStyle: data.data.languageStyle,
            firstLetter: data.data.firstLetter,
            gender: data.data.gender.toLowerCase(),
            language: data.data.language === 'CHINESE' ? 'Chinese' : 'English',
            username: data.data.userAppellation || ''
          };
          setCharacter(characterData);
        } else {
          throw new Error(data.message || '加载失败');
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchCharacter();
  }, [id]);

  const handleEdit = () => {
    // TODO: 跳转到编辑页面
    navigate(`/character/${id}/edit`);
  };

  const handleWriteLetter = () => {
    // TODO: 跳转到写信页面
    navigate(`/letter/create/${id}`);
  };

  if (loading) {
    return (
      <Box sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh'
      }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  if (!character) {
    return null;
  }

  return (
    <Box sx={{
      p: 3,
      maxWidth: '1200px',
      margin: '0 auto',
      backgroundColor: '#fff',
    }}>
      {/* 标题栏 */}
      <Box sx={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        mb: 4
      }}>
        <Typography variant="h5">
          {character?.name} - {t('character.detail.title')}
        </Typography>

        {/* 按钮组 */}
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<MailIcon />}
            onClick={handleWriteLetter}
            sx={{
              minWidth: 140,
              height: 40,
              borderRadius: 2,
            }}
          >
            {t('character.detail.writeLetter')}
          </Button>

          <Button
            variant="outlined"
            color="primary"
            startIcon={<EditIcon />}
            onClick={handleEdit}
            sx={{
              minWidth: 100,
              height: 40,
              borderRadius: 2,
            }}
          >
            {t('character.detail.edit')}
          </Button>
        </Box>
      </Box>

      <Box sx={{ display: 'flex', gap: 3 }}>
        {/* 左列 */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* 角色名称 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.name.label')}</Label>
              <TextField
                value={character.name}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 角色头像 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.avatar.label')}</Label>
              <Paper
                sx={{
                  p: 2,
                  border: '1px dashed',
                  borderColor: 'divider',
                  textAlign: 'center',
                  backgroundColor: '#fff',
                }}
              >
                <Box
                  sx={{
                    width: '300px',
                    height: '400px',
                    margin: '0 auto',
                    borderRadius: '4px',
                    overflow: 'hidden',
                    backgroundColor: '#f5f5f5',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  <img
                    src={character.avatar}
                    alt={t('character.create.avatar.label')}
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                    }}
                  />
                </Box>
              </Paper>
            </FormControl>
          </Paper>

          {/* 角色背景 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.background.label')}</Label>
              <TextField
                value={character.background}
                multiline
                rows={8}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 性格特征 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.personalityTraits.label')}</Label>
              <TextField
                value={character.personalityTraits}
                multiline
                rows={4}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 角色描述 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.description.label')}</Label>
              <TextField
                value={character.description}
                multiline
                rows={4}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>
        </Box>

        {/* 右列 */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* 角色性别 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.gender.label')}</Label>
              <RadioGroup
                row
                value={character.gender}
              >
                <FormControlLabel
                  value="male"
                  control={<Radio disabled />}
                  label={<Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    💙 {t('character.create.gender.male')}
                  </Box>}
                />
                <FormControlLabel
                  value="female"
                  control={<Radio disabled />}
                  label={<Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    💗 {t('character.create.gender.female')}
                  </Box>}
                />
                <FormControlLabel
                  value="special"
                  control={<Radio disabled />}
                  label={<Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    🌈 {t('character.create.gender.special')}
                  </Box>}
                />
              </RadioGroup>
            </FormControl>
          </Paper>

          {/* 角色语言 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.language.label')}</Label>
              <RadioGroup
                row
                value={character.language}
              >
                <FormControlLabel
                  value="Chinese"
                  control={<Radio disabled />}
                  label={t('character.create.language.zh')}
                />
                <FormControlLabel
                  value="English"
                  control={<Radio disabled />}
                  label={t('character.create.language.en')}
                />
              </RadioGroup>
            </FormControl>
          </Paper>

          {/* 对用户称呼 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.username.label')}</Label>
              <TextField
                value={character.username}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 角色代码 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.code.label')}</Label>
              <TextField
                value={character.code}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 语言风格 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.languageStyle.label')}</Label>
              <TextField
                value={character.languageStyle}
                multiline
                rows={4}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>

          {/* 第一封信件 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.firstLetter.label')}</Label>
              <TextField
                value={character.firstLetter}
                multiline
                rows={6}
                fullWidth
                InputProps={{
                  readOnly: true,
                }}
              />
            </FormControl>
          </Paper>
        </Box>
      </Box>
    </Box>
  );
};

export default CharacterDetail; 