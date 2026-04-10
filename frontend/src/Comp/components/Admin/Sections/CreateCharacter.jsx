import React, { useState } from 'react';
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
import { createCharacter } from '../../../../services/characterService';
import { useToast } from '../../../../Comp/components/General/Business/ToastContext';

const RequiredLabel = ({ children }) => (
  <FormLabel 
    required 
    sx={{ 
      mb: 1,
      '& .MuiFormLabel-asterisk': {
        color: 'error.main'
      }
    }}
  >
    {children}
  </FormLabel>
);

const CreateCharacter = () => {
  const { t } = useTranslation();
  const { showToast } = useToast();
  const [formData, setFormData] = useState({
    code: '',
    name: '',
    avatar: '',
    background: '',
    description: '',
    personalityTraits: '',
    languageStyle: '',
    firstLetter: '',
    gender: 'male',
    language: 'Chinese',
    username: '',
  });

  // 添加图片加载状态
  const [imageError, setImageError] = useState(false);
  const [isImageLoading, setIsImageLoading] = useState(false);

  // 首先添加错误状态
  const [errors, setErrors] = useState({
    code: '',
    name: '',
    avatar: '',
    background: '',
    description: '',
    personalityTraits: '',
    languageStyle: '',
    firstLetter: '',
    username: '',
  });

  // 添加字段验证规则
  const validations = {
    code: {
      required: true,
      max: 12,
    },
    name: {
      required: true,
      min: 2,
      max: 10,
    },
    avatar: {
      required: true,
    },
    background: {
      required: true,
      min: 64,
      max: 1024,
    },
    description: {
      required: true,
      min: 10,
      max: 100
    },
    personalityTraits: {
      required: true,
      min: 64,
      max: 1024
    },
    languageStyle: {
      required: true,
      min: 64,
      max: 512,
    },
    firstLetter: {
      required: true,
      min: 0,
      max: 512,
    },
    username: {
      required: false,
      min: 0,
      max: 10,
    },
    gender: {
      required: true,
    },
    language: {
      required: true,
    }
  };

  // 修改 handleChange 函数，添加长度限制
  const handleChange = (field) => (event) => {
    const value = event.target.value;
    const validation = validations[field];
    
    // 如果超出最大长度，不更新值
    if (validation?.max && value.length > validation.max) {
      return;
    }

    setFormData(prev => ({
      ...prev,
      [field]: value
    }));

    // 实时验证字段长度
    validateField(field, value);

    // 图片处理逻辑保持不变
    if (field === 'avatar') {
      setImageError(false);
      setIsImageLoading(true);
    }
  };

  // 修改 validateField 函数
  const validateField = (field, value) => {
    const validation = validations[field];
    if (!validation) return;

    let error = '';
    if (validation.required && !value) {
      error = t('common.required');
    } else if (validation.min && value.length < validation.min) {
      error = `不能少于${validation.min}个字`;  // 直接显示中文提示
    }

    setErrors(prev => ({
      ...prev,
      [field]: error
    }));
  };

  // 添加提交函数
  const handleSubmit = async (event) => {
    event.preventDefault();

    // 验证所有必填字段
    let hasError = false;
    Object.keys(validations).forEach(field => {
      validateField(field, formData[field]);
      if (validations[field].required && !formData[field]) {
        hasError = true;
      }
    });

    if (hasError) {
      showToast(t('common.required'), 'error');
      return;
    }

    try {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        showToast(t('auth.loginRequired'), 'error');
        return;
      }

      const characterData = {
        name: formData.name,
        code: formData.code,
        description: formData.description,
        avatar_url: formData.avatar,
        status: 'PUBLIC',
        backgroundStory: formData.background,
        personalityTraits: formData.personalityTraits,
        languageStyle: formData.languageStyle,
        firstLetter: formData.firstLetter,
        language: formData.language.toUpperCase(),
        userAppellation: formData.username || '',
        gender: formData.gender,
        token: token
      };

      await createCharacter(characterData);
      showToast(t('character.create.success'), 'success');
      window.location.href = '/';
    } catch (error) {
      console.error('Failed to create character:', error);
      showToast(t('character.create.failed'), 'error');
    }
  };

  // 处理图片加载错误
  const handleImageError = () => {
    setImageError(true);
    setIsImageLoading(false);
  };

  // 处理图片加载完成
  const handleImageLoad = () => {
    setIsImageLoading(false);
  };

  return (
    <Box sx={{ 
      p: 3, 
      maxWidth: '1200px',
      margin: '0 auto',
      backgroundColor: '#fff',
    }}>
      {/* 标题栏，包含标题和保存按钮 */}
      <Box sx={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        mb: 4 
      }}>
        <Typography variant="h5">
          {t('character.create.title')}
        </Typography>
        
        <Button
          type="submit"
          variant="contained"
          size="large"
          onClick={handleSubmit}
          sx={{
            minWidth: 120,
            height: 40,
            borderRadius: 2,
          }}
        >
          {t('common.save')}
        </Button>
      </Box>

      <Box component="form" sx={{ display: 'flex', gap: 3 }}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* 角色名称 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.name.label')}</RequiredLabel>
              <TextField
                placeholder={t('character.create.name.placeholder')}
                value={formData.name}
                onChange={handleChange('name')}
                fullWidth
                error={!!errors.name}
                helperText={
                  errors.name || 
                  (validations.name.min ? 
                    `(${formData.name.length}/${validations.name.max}) 最少${validations.name.min}字` :
                    `(${formData.name.length}/${validations.name.max})`
                  )
                }
              />
            </FormControl>
          </Paper>

          {/* 角色头像 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.avatar.label')}</RequiredLabel>
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
                    margin: '0 auto 16px',
                    borderRadius: '4px',
                    overflow: 'hidden',
                    backgroundColor: '#f5f5f5', // 更改预览区域背景色
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  {formData.avatar && !imageError ? (
                    <>
                      <img
                        src={formData.avatar}
                        alt={t('character.create.avatar.label')}
                        style={{
                          width: '100%',
                          height: '100%',
                          objectFit: 'cover',
                          display: isImageLoading ? 'none' : 'block',
                        }}
                        onError={handleImageError}
                        onLoad={handleImageLoad}
                      />
                      {isImageLoading && (
                        <Typography sx={{ color: 'grey.500' }}>
                          {t('character.create.avatar.loading')}
                        </Typography>
                      )}
                    </>
                  ) : (
                    <Typography sx={{ color: 'grey.500' }}>
                      {imageError ? t('character.create.avatar.error') : ''}
                    </Typography>
                  )}
                </Box>

                <Typography sx={{ color: 'text.secondary', fontSize: '0.875rem', mb: 2 }}>
                  建议上传图片宽高比为 3:4，或角色主题位于图片中心位置。
                </Typography>

                <TextField
                  placeholder={t('character.create.avatar.linkPlaceholder')}
                  value={formData.avatar}
                  onChange={handleChange('avatar')}
                  error={!!errors.avatar || imageError}
                  helperText={errors.avatar || (imageError ? t('character.create.avatar.error') : '')}
                  fullWidth
                  sx={{
                    '& .MuiInputBase-input': {
                      backgroundColor: '#fff',
                    }
                  }}
                />
              </Paper>
            </FormControl>
          </Paper>

          {/* 角色背景 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.background.label')}</RequiredLabel>
              <TextField
                placeholder={t('character.create.background.placeholder')}
                value={formData.background}
                onChange={handleChange('background')}
                fullWidth
                multiline
                rows={8}
                error={!!errors.background}
                helperText={
                  errors.background || 
                  (validations.background.min ? 
                    `(${formData.background.length}/${validations.background.max}) 最少${validations.background.min}字` :
                    `(${formData.background.length}/${validations.background.max})`
                  )
                }
              />
            </FormControl>
          </Paper>

          {/* 角色设定 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.personalityTraits.label')}</RequiredLabel>
              <TextField
                placeholder={t('character.create.personalityTraits.placeholder')}
                multiline
                rows={4}
                value={formData.personalityTraits}
                onChange={handleChange('personalityTraits')}
                fullWidth
                error={!!errors.personalityTraits}
                helperText={
                  errors.personalityTraits || 
                  (validations.personalityTraits.min ? 
                    `(${formData.personalityTraits.length}/${validations.personalityTraits.max}) 最少${validations.personalityTraits.min}字` :
                    `(${formData.personalityTraits.length}/${validations.personalityTraits.max})`
                  )
                }
              />
            </FormControl>
          </Paper>

          {/* 角色描述 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.description.label')}</RequiredLabel>
              <TextField
                placeholder={t('character.create.description.placeholder')}
                multiline
                rows={4}
                value={formData.description}
                onChange={handleChange('description')}
                fullWidth
                error={!!errors.description}
                helperText={
                  errors.description || 
                  (validations.description.min ? 
                    `(${formData.description.length}/${validations.description.max}) 最少${validations.description.min}字` :
                    `(${formData.description.length}/${validations.description.max})`
                  )
                }
              />
            </FormControl>
          </Paper>
        </Box>

        {/* 右列 */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* 角色性别 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.gender.label')}</RequiredLabel>
              <RadioGroup
                row
                value={formData.gender}
                onChange={handleChange('gender')}
              >
                <FormControlLabel 
                  value="male" 
                  control={<Radio />} 
                  label={<Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    💙 {t('character.create.gender.male')}
                  </Box>} 
                />
                <FormControlLabel 
                  value="female" 
                  control={<Radio />} 
                  label={<Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    💗 {t('character.create.gender.female')}
                  </Box>} 
                />
                <FormControlLabel 
                  value="special" 
                  control={<Radio />} 
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
              <RequiredLabel>{t('character.create.language.label')}</RequiredLabel>
              <RadioGroup
                row
                value={formData.language}
                onChange={handleChange('language')}
              >
                <FormControlLabel 
                  value="Chinese" 
                  control={<Radio />} 
                  label={t('character.create.language.zh')}
                />
                <FormControlLabel 
                  value="English" 
                  control={<Radio />} 
                  label={t('character.create.language.en')}
                />
              </RadioGroup>
            </FormControl>
          </Paper>

          {/* 对用户称呼 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <FormLabel sx={{ mb: 1 }}>
                {t('character.create.username.label')}
              </FormLabel>
              <TextField
                placeholder={`${t('character.create.username.placeholder')}（${t('character.create.username.hint')}）`}
                value={formData.username}
                onChange={handleChange('username')}
                fullWidth
                helperText={
                  errors.username || 
                  (validations.username.max ? 
                    `(${formData.username.length}/${validations.username.max})` :
                    `(${formData.username.length}/${validations.username.max})`
                  )
                }
                error={!!errors.username}
              />
            </FormControl>
          </Paper>

          {/* 角色代码 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.code.label')}</RequiredLabel>
              <TextField
                placeholder={t('character.create.code.placeholder')}
                value={formData.code}
                onChange={handleChange('code')}
                fullWidth
                error={!!errors.code}
                helperText={
                  errors.code || 
                  `(${formData.code.length}/${validations.code.max})`
                }
              />
            </FormControl>
          </Paper>

          {/* 语言风格 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.languageStyle.label')}</RequiredLabel>
              <TextField
                multiline
                rows={4}
                placeholder={`${t('character.create.languageStyle.placeholder')}（${t('character.create.languageStyle.hint')}）`}
                value={formData.languageStyle}
                onChange={handleChange('languageStyle')}
                fullWidth
                helperText={
                  errors.languageStyle || 
                  (validations.languageStyle.max ? 
                    `(${formData.languageStyle.length}/${validations.languageStyle.max})` :
                    `(${formData.languageStyle.length}/${validations.languageStyle.max})`
                  )
                }
                error={!!errors.languageStyle}
              />
            </FormControl>
          </Paper>

          {/* 第一封信件 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <RequiredLabel>{t('character.create.firstLetter.label')}</RequiredLabel>
              <TextField
                multiline
                rows={6}
                placeholder={`${t('character.create.firstLetter.placeholder')}（${t('character.create.firstLetter.hint')}）`}
                value={formData.firstLetter}
                onChange={handleChange('firstLetter')}
                fullWidth
                helperText={
                  errors.firstLetter || 
                  (validations.firstLetter.max ? 
                    `(${formData.firstLetter.length}/${validations.firstLetter.max})` :
                    `(${formData.firstLetter.length}/${validations.firstLetter.max})`
                  )
                }
                error={!!errors.firstLetter}
              />
            </FormControl>
          </Paper>
        </Box>
      </Box>
    </Box>
  );
};

export default CreateCharacter; 