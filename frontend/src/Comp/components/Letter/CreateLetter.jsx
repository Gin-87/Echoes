import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  TextField,
  Paper,
  Button,
  CircularProgress,
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  Tooltip,
  IconButton,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import SendIcon from '@mui/icons-material/Send';
import { useToast } from '../General/Business/ToastContext';
import request from '../../../utils/request';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';

// 复用标签组件
const Label = ({ children }) => (
  <FormLabel sx={{ mb: 1 }}>{children}</FormLabel>
);

// 在文件顶部添加邮箱验证的正则表达式
const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

// 在标题后添加必填标记的样式组件
const RequiredLabel = ({ children }) => (
  <Box component="span" sx={{ display: 'flex', alignItems: 'center' }}>
    {children}
    <Typography 
      component="span" 
      sx={{ 
        color: 'error.main',
        ml: 0.5,
        fontSize: 'inherit'
      }}
    >
      *
    </Typography>
  </Box>
);

const CreateLetter = () => {
  const { id } = useParams();  // 获取角色ID
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { showToast } = useToast();
  
  const [character, setCharacter] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [content, setContent] = useState('');
  const [frequency, setFrequency] = useState('week'); // 默认每周
  const [email, setEmail] = useState('');
  const [emailError, setEmailError] = useState(false);
  const [taskName, setTaskName] = useState('');

  // 加载角色信息
  useEffect(() => {
    const fetchCharacter = async () => {
      try {
        setLoading(true);
        const response = await request.get(`/api/characters/getDetail/${id}`);

        if (response.code === 200) {
          setCharacter({
            id: id,
            name: response.data.name,
            code: response.data.code,
            avatar: response.data.avatar_url,
            background: response.data.backgroundStory,
            personalityTraits: response.data.personalityTraits,
            description: response.data.description,
            languageStyle: response.data.languageStyle,
            username: response.data.userAppellation || ''
          });
        } else {
          throw new Error(response.message || '加载失败');
        }
      } catch (error) {
        showToast(error.message, 'error');
      } finally {
        setLoading(false);
      }
    };

    fetchCharacter();
  }, [id]);

  // 邮箱验证函数
  const validateEmail = (email) => {
    return EMAIL_REGEX.test(email);
  };

  // 处理邮箱变化
  const handleEmailChange = (e) => {
    const value = e.target.value;
    setEmail(value);
    if (value && !validateEmail(value)) {
      setEmailError(true);
    } else {
      setEmailError(false);
    }
  };

  // 提交信件
  const handleSubmit = async () => {
    // 任务名称校验
    if (!taskName.trim() || taskName.length > 15) {
      showToast(t('task.name.required'), 'warning');
      return;
    }

    // 对用户称呼校验
    if (!character?.username?.trim()) {
      showToast(t('task.appellation.required'), 'warning');
      return;
    }

    // 收信地址校验
    if (!email || !validateEmail(email)) {
      showToast(t('task.email.required'), 'warning');
      return;
    }

    // 关系描述校验
    if (!content.trim() || content.length < 10) {
      showToast(t('task.relationshipRequired'), 'warning');
      return;
    }

    try {
      setSubmitting(true);
      const response = await request.post('/api/tasks/create', {
        characterId: id,
        relationshipWithUser: content,
        frequency: ({week:"WEEKLY",halfWeek:"HALFWEEKLY",halfMonth:"HALFMONTHLY",month:"MONTHLY",random:"RANDOM"})[frequency] || "WEEKLY",
        email: email,
        taskName: taskName,
        finalAppellation: character?.userAppellation || "友人"
      });

      if (response.code === 200) {
        showToast(t('task.createSuccess'), 'success');
        navigate(`/task/${response.data.id}`);  // 跳转到任务详情页
      } else {
        throw new Error(response.message || t('task.createFailed'));
      }
    } catch (error) {
      showToast(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  };

  // 不可编辑文本框的样式修改
  const readOnlyTextFieldStyle = {
    '.MuiInputBase-root': {
      backgroundColor: '#f5f5f5',
      cursor: 'default',
      userSelect: 'text',
      '&:hover': {
        backgroundColor: '#f5f5f5',
        fieldset: {
          borderColor: 'rgba(0, 0, 0, 0.23) !important',
        }
      },
      '&.Mui-focused': {
        backgroundColor: '#f5f5f5',
        fieldset: {
          borderColor: 'rgba(0, 0, 0, 0.23) !important',
          borderWidth: '1px !important',
        }
      },
    },
  };

  // 在 useEffect 中设置默认任务名
  useEffect(() => {
    if (character?.name) {
      const defaultTaskName = t('task.defaultTaskName').replace('{name}', character.name);
      setTaskName(defaultTaskName);
    }
  }, [character?.name]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ 
      p: 3, 
      width: '60%',  // 留出两侧各20%空白
      margin: '0 auto', 
      backgroundColor: '#fff' 
    }}>
      {/* 标题栏 */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Typography variant="h5">
          {t('task.letterTitle').replace('{name}', character?.name || '')}
          <Typography 
            component="span" 
            sx={{ 
              ml: 1,
              color: 'text.secondary',
              fontSize: '0.8em'
            }}
          >
            @{character?.code}
          </Typography>
        </Typography>
      </Box>

      <Box sx={{ display: 'flex', gap: 3 }}>
        {/* 左侧：角色信息 */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* 角色头像 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.avatar.label')}</Label>
              <Box sx={{
                width: '50%',  // 头像宽度减少50%
                margin: '0 auto',  // 居中显示
                height: '460px',  // 增加15%的高度 (400 * 1.15 = 460)
                position: 'relative',
                borderRadius: '4px',
                overflow: 'hidden',
                backgroundColor: '#f5f5f5',
              }}>
                <img
                  src={character?.avatar}
                  alt={character?.name}
                  style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                  }}
                />
              </Box>
            </FormControl>
          </Paper>

          {/* 角色背景 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.background.label')}</Label>
              <TextField
                value={character?.background}
                multiline
                rows={4}
                fullWidth
                InputProps={{ 
                  readOnly: true,
                  sx: { cursor: 'default' }
                }}
                sx={readOnlyTextFieldStyle}
              />
            </FormControl>
          </Paper>

          {/* 角色设定 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.personalityTraits.label')}</Label>
              <TextField
                value={character?.personalityTraits}
                multiline
                rows={4}
                fullWidth
                InputProps={{ 
                  readOnly: true,
                  sx: { cursor: 'default' }
                }}
                sx={readOnlyTextFieldStyle}
              />
            </FormControl>
          </Paper>

          {/* 角色描述 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.description.label')}</Label>
              <TextField
                value={character?.description}
                multiline
                rows={4}
                fullWidth
                InputProps={{ 
                  readOnly: true,
                  sx: { cursor: 'default' }
                }}
                sx={readOnlyTextFieldStyle}
              />
            </FormControl>
          </Paper>

          {/* 语言风格 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider' }}>
            <FormControl fullWidth>
              <Label>{t('character.create.languageStyle.label')}</Label>
              <TextField
                value={character?.languageStyle}
                multiline
                rows={4}
                fullWidth
                InputProps={{ 
                  readOnly: true,
                  sx: { cursor: 'default' }
                }}
                sx={readOnlyTextFieldStyle}
              />
            </FormControl>
          </Paper>
        </Box>

        {/* 右侧：写信区域 */}
        <Box sx={{ flex: 1 }}>
          {/* 任务名称 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', mb: 3 }}>
            <FormControl fullWidth>
              <Box sx={{ display: 'flex', alignItems: 'baseline' }}>
                <Typography 
                  component="span" 
                  sx={{ 
                    fontWeight: 500,
                    whiteSpace: 'nowrap'
                  }}
                >
                  <RequiredLabel>{t('task.name.label')}</RequiredLabel>
                </Typography>
                <Typography 
                  component="span" 
                  sx={{ 
                    ml: 2,
                    color: '#999999',
                    fontSize: '0.875rem',
                    whiteSpace: 'nowrap'
                  }}
                >
                  {t('task.name.hint')}
                </Typography>
              </Box>
              <TextField
                value={taskName}
                onChange={(e) => setTaskName(e.target.value)}
                fullWidth
                error={taskName.length > 0 && (taskName.length < 1 || taskName.length > 15)}
                helperText={
                  taskName.length > 0 && taskName.length < 1
                    ? t('task.name.tooShort')
                    : taskName.length > 15
                      ? t('task.name.tooLong')
                      : ''
                }
                inputProps={{
                  maxLength: 15
                }}
                sx={{ mt: 1 }}
              />
            </FormControl>
          </Paper>

          {/* 对用户称呼 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', mb: 3 }}>
            <FormControl fullWidth>
              <Label>
                <RequiredLabel>
                  {t('task.Appellation').replace('{name}', character?.name || '')}
                </RequiredLabel>
              </Label>
              <TextField
                value={character?.username}
                onChange={(e) => setCharacter(prev => ({
                  ...prev,
                  username: e.target.value
                }))}
                fullWidth
                error={character?.username?.length > 0 && (character?.username?.length < 1 || character?.username?.length > 10)}
                helperText={
                  character?.username?.length > 0 && character?.username?.length < 1
                    ? t('character.create.username.tooShort')
                    : character?.username?.length > 10
                      ? t('character.create.username.tooLong')
                      : t('character.create.username.hint')
                }
                inputProps={{
                  maxLength: 10
                }}
              />
            </FormControl>
          </Paper>

          {/* 添加收信地址 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', mb: 3 }}>
            <FormControl fullWidth>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'baseline' }}>
                  <Typography 
                    component="span" 
                    sx={{ 
                      fontWeight: 500,
                      whiteSpace: 'nowrap'
                    }}
                  >
                    <RequiredLabel>{t('task.email.label')}</RequiredLabel>
                  </Typography>
                  <Typography 
                    component="span" 
                    sx={{ 
                      ml: 2,
                      color: '#999999',
                      fontSize: '0.875rem'
                    }}
                  >
                    {t('task.email.hint')}
                  </Typography>
                </Box>
                <TextField
                  value={email}
                  onChange={handleEmailChange}
                  fullWidth
                  placeholder="example.name@example.com"
                  error={emailError}
                  helperText={emailError ? t('task.email.invalid') : ''}
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      '&.Mui-error .MuiOutlinedInput-notchedOutline': {
                        borderColor: 'error.main'
                      }
                    }
                  }}
                />
              </Box>
            </FormControl>
          </Paper>

          {/* 添加频率选择 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', mb: 3 }}>
            <FormControl fullWidth>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'baseline' }}>
                  <Typography 
                    component="span" 
                    sx={{ 
                      fontWeight: 500,
                      whiteSpace: 'nowrap'  // 防止标题换行
                    }}
                  >
                    {t('task.frequency.label')}
                  </Typography>
                  <Typography 
                    component="span" 
                    sx={{ 
                      ml: 2,
                      color: '#999999',
                      fontSize: '0.875rem',
                      whiteSpace: 'nowrap'  // 防止提示文字换行
                    }}
                  >
                    {t('task.frequency.hint')}
                  </Typography>
                </Box>
                <RadioGroup
                  row
                  value={frequency}
                  onChange={(e) => setFrequency(e.target.value)}
                  sx={{
                    flexWrap: 'nowrap',  // 防止选项换行
                    '& .MuiFormControlLabel-root': {
                      whiteSpace: 'nowrap'  // 防止选项文字换行
                    }
                  }}
                >
                  <FormControlLabel 
                    value="halfWeek" 
                    control={
                      <Radio 
                        sx={{
                          color: 'rgba(0, 0, 0, 0.6)',
                          '&.Mui-checked': {
                            color: 'rgba(0, 0, 0, 0.6)'
                          }
                        }}
                      />
                    } 
                    label={t('task.frequency.halfWeek')} 
                  />
                  <FormControlLabel 
                    value="week" 
                    control={
                      <Radio 
                        sx={{
                          color: 'rgba(0, 0, 0, 0.6)',
                          '&.Mui-checked': {
                            color: 'rgba(0, 0, 0, 0.6)'
                          }
                        }}
                      />
                    } 
                    label={t('task.frequency.week')} 
                  />
                  <FormControlLabel 
                    value="halfMonth" 
                    control={
                      <Radio 
                        sx={{
                          color: 'rgba(0, 0, 0, 0.6)',
                          '&.Mui-checked': {
                            color: 'rgba(0, 0, 0, 0.6)'
                          }
                        }}
                      />
                    } 
                    label={t('task.frequency.halfMonth')} 
                  />
                  <FormControlLabel 
                    value="month" 
                    control={
                      <Radio 
                        sx={{
                          color: 'rgba(0, 0, 0, 0.6)',
                          '&.Mui-checked': {
                            color: 'rgba(0, 0, 0, 0.6)'
                          }
                        }}
                      />
                    } 
                    label={t('task.frequency.month')} 
                  />
                  <FormControlLabel 
                    value="random" 
                    control={
                      <Radio 
                        sx={{
                          color: 'rgba(0, 0, 0, 0.6)',
                          '&.Mui-checked': {
                            color: 'rgba(0, 0, 0, 0.6)'
                          }
                        }}
                      />
                    } 
                    label={t('task.frequency.random')} 
                  />
                </RadioGroup>
              </Box>
            </FormControl>
          </Paper>

          {/* 关系描述 */}
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', mb: 3 }}>
            <FormControl fullWidth>
              <Label>
                <RequiredLabel>
                  {t('task.relationshipTitle').replace('{name}', character?.name || '')}
                </RequiredLabel>
              </Label>
              <TextField
                value={content}
                onChange={(e) => setContent(e.target.value)}
                multiline
                rows={25}
                fullWidth
                placeholder={t('task.relationshipContentHint')}
                error={content.length > 0 && content.length < 10}
                helperText={
                  content.length > 0 && content.length < 10 
                    ? t('task.relationshipMinHint')
                    : t('task.relationshipHint')
                }
                inputProps={{
                  maxLength: 1024
                }}
              />
            </FormControl>
          </Paper>

          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
            <Button
              variant="contained"
              color="primary"
              startIcon={<SendIcon />}
              onClick={handleSubmit}
              disabled={
                submitting || 
                content.length < 10 || 
                content.length > 1024 ||
                (character?.username?.length > 0 && character?.username?.length > 10)
              }
              sx={{ minWidth: 120 }}
            >
              {submitting ? t('common.submitting') : t('task.send')}
            </Button>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default CreateLetter; 