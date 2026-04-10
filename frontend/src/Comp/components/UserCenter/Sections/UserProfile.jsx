import React from 'react';
import { 
  Box, 
  Grid, 
  Typography, 
  TextField, 
  Button, 
  Select,
  MenuItem,
  FormControl,
  Stack,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Menu,
  InputAdornment
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from '@mui/icons-material/Close';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import { changeLanguage } from '../../../../utils/i18n/i18n.js';

function UserProfile() {
  const { t, i18n } = useTranslation();
  
  // 模拟数据，实际应从后端获取
  const userInfo = {
    nickname: '昵称',
    email: '',  // 未绑定
    phone: ''   // 未绑定
  };

  // 状态定义
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [openBindDialog, setOpenBindDialog] = React.useState(false);
  const [bindType, setBindType] = React.useState('');
  const [countdown, setCountdown] = React.useState(0);
  const [verificationCode, setVerificationCode] = React.useState('');
  const [bindValue, setBindValue] = React.useState('');
  const [emailInput, setEmailInput] = React.useState('');
  const [phoneInput, setPhoneInput] = React.useState('');
  const [nickname, setNickname] = React.useState(userInfo.nickname || '');
  const [nicknameError, setNicknameError] = React.useState('');

  // 验证正则
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  const phoneRegex = /^1[3-9]\d{9}$/;
  const nicknameRegex = /^[\u4e00-\u9fa5a-zA-Z0-9]{1,10}$/;

  // 验证函数
  const isEmailValid = (email) => emailRegex.test(email);
  const isPhoneValid = (phone) => phoneRegex.test(phone);

  // 处理邮箱输入
  const handleEmailChange = (event) => {
    setEmailInput(event.target.value);
  };

  // 处理手机号输入
  const handlePhoneChange = (event) => {
    const value = event.target.value.replace(/\D/g, ''); // 只允许输入数字
    if (value.length <= 11) { // 限制长度为11
      setPhoneInput(value);
    }
  };

  // 处理验证码输入
  const handleVerificationCodeChange = (event) => {
    const value = event.target.value.replace(/\D/g, ''); // 只允许输入数字
    setVerificationCode(value);
  };

  const handleLanguageClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleLanguageClose = () => {
    setAnchorEl(null);
  };

  const handleLanguageChange = (lang) => {
    changeLanguage(lang);
    setAnchorEl(null);
  };

  const handleNicknameChange = (event) => {
    const value = event.target.value;
    setNickname(value);
    
    if (!value) {
      setNicknameError(t('userCenter.profileSection.nicknameRequired'));
    } else if (!nicknameRegex.test(value)) {
      setNicknameError(t('userCenter.profileSection.nicknameInvalid'));
    } else {
      setNicknameError('');
    }
  };

  const handleSaveNickname = () => {
    if (!nicknameError && nickname) {
      // TODO: 实现保存昵称逻辑
    }
  };

  const handleSave = () => {
    // TODO: 实现保存逻辑
  };

  const handleBind = (type) => {
    const value = type === 'email' ? emailInput : phoneInput;
    setBindValue(value);
    setBindType(type);
    setOpenBindDialog(true);
    startCountdown();
  };

  const handleCloseDialog = () => {
    setOpenBindDialog(false);
    setVerificationCode('');
    setCountdown(0);
  };

  const startCountdown = () => {
    setCountdown(60);
    const timer = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
  };

  const handleResendCode = () => {
    // TODO: 实现重新发送验证码逻辑
    startCountdown();
  };

  const handleVerify = () => {
    // TODO: 实现验证逻辑
    handleCloseDialog();
  };

  // 渲染手机号绑定按钮的条件
  const shouldShowPhoneBinding = i18n.language === 'zh';

  // 格式化手机号显示
  const formatPhoneNumber = (phone) => {
    if (!phone) return '';
    return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
  };

  // 格式化邮箱显示
  const formatEmail = (email) => {
    if (!email) return '';
    const [username, domain] = email.split('@');
    if (username.length <= 3) {
      return `${username}***@${domain}`;
    }
    return `${username.slice(0, 3)}***@${domain}`;
  };

  return (
    <Box>
      <Grid container spacing={4}>
        <Grid item xs={12}>
          {/* 昵称 */}
          <Box sx={{ mb: 5 }}>
            <Typography 
              variant="subtitle1" 
              sx={{ 
                mb: 2,
                fontSize: '1rem',
                fontWeight: 500,
                color: 'rgba(0, 0, 0, 0.7)'
              }}
            >
              {t('userCenter.profileSection.nickname')}
            </Typography>
            <Stack direction="row" spacing={2} alignItems="flex-start">
              <TextField 
                size="small"
                fullWidth
                value={nickname}
                onChange={handleNicknameChange}
                error={Boolean(nicknameError)}
                helperText={nicknameError}
                placeholder={t('userCenter.profileSection.nicknamePlaceholder')}
                sx={{ 
                  maxWidth: 300,
                  '& .MuiInputBase-root': {
                    fontSize: '0.95rem'
                  },
                  '& .MuiInputBase-input::placeholder': {
                    fontSize: '0.85rem',
                    opacity: 0.7
                  }
                }}
                inputProps={{
                  maxLength: 10
                }}
              />
              <Button 
                variant="contained"
                size="small"
                startIcon={<SaveIcon sx={{ fontSize: 18 }} />}
                onClick={handleSaveNickname}
                disabled={!!nicknameError || !nickname}
                sx={{
                  width: 120,
                  py: 1,
                  textTransform: 'none',
                  fontWeight: 500,
                  boxShadow: 'none',
                  '&:hover': {
                    boxShadow: 'none'
                  }
                }}
              >
                {t('userCenter.profileSection.save')}
              </Button>
            </Stack>
          </Box>

          {/* 邮箱 */}
          <Box sx={{ mb: 5 }}>
            <Typography 
              variant="subtitle1" 
              sx={{ 
                mb: 2,
                fontSize: '1rem',
                fontWeight: 500,
                color: 'rgba(0, 0, 0, 0.7)'
              }}
            >
              {t('userCenter.profileSection.email')}
            </Typography>
            <Stack direction="row" spacing={2} alignItems="center">
              <TextField
                size="small"
                fullWidth
                value={userInfo.email || emailInput}
                onChange={handleEmailChange}
                placeholder={t('userCenter.profileSection.emailPlaceholder')}
                sx={{ 
                  maxWidth: 300,
                  '& .MuiInputBase-root': {
                    fontSize: '0.95rem',
                    backgroundColor: userInfo.email ? 'rgba(0, 0, 0, 0.03)' : 'inherit'
                  },
                  '& .MuiInputBase-input::placeholder': {
                    fontSize: '0.85rem',
                    opacity: 0.7
                  }
                }}
                disabled={!!userInfo.email}
                error={Boolean(emailInput && !isEmailValid(emailInput))}
                helperText={emailInput && !isEmailValid(emailInput) ? t('userCenter.profileSection.invalidEmail') : ''}
              />
              {!userInfo.email && (
                <Button 
                  variant="outlined"
                  size="small"
                  onClick={() => handleBind('email')}
                  disabled={!emailInput || !isEmailValid(emailInput)}
                  sx={{
                    width: 120,
                    py: 1,
                    textTransform: 'none',
                    fontWeight: 500,
                    borderRadius: 1,
                    borderColor: 'primary.main',
                    '&:hover': {
                      borderColor: 'primary.dark',
                      backgroundColor: 'rgba(25, 118, 210, 0.04)'
                    }
                  }}
                >
                  {t('userCenter.profileSection.bindEmail')}
                </Button>
              )}
            </Stack>
          </Box>

          {/* 手机号 */}
          {shouldShowPhoneBinding && (
            <Box sx={{ mb: 5 }}>
              <Typography 
                variant="subtitle1" 
                sx={{ 
                  mb: 2,
                  fontSize: '1rem',
                  fontWeight: 500,
                  color: 'rgba(0, 0, 0, 0.7)'
                }}
              >
                {t('userCenter.profileSection.phone')}
              </Typography>
              <Stack direction="row" spacing={2} alignItems="center">
                <TextField
                  size="small"
                  fullWidth
                  value={userInfo.phone || phoneInput}
                  onChange={handlePhoneChange}
                  placeholder={t('userCenter.profileSection.phonePlaceholder')}
                  sx={{ 
                    maxWidth: 300,
                    '& .MuiInputBase-root': {
                      fontSize: '0.95rem',
                      backgroundColor: userInfo.phone ? 'rgba(0, 0, 0, 0.03)' : 'inherit'
                    },
                    '& .MuiInputBase-input::placeholder': {
                      fontSize: '0.85rem',
                      opacity: 0.7
                    }
                  }}
                  disabled={!!userInfo.phone}
                  error={Boolean(phoneInput && !isPhoneValid(phoneInput))}
                  helperText={phoneInput && !isPhoneValid(phoneInput) ? t('userCenter.profileSection.invalidPhone') : ''}
                />
                {!userInfo.phone && (
                  <Button 
                    variant="outlined"
                    size="small"
                    onClick={() => handleBind('phone')}
                    disabled={!phoneInput || !isPhoneValid(phoneInput)}
                    sx={{
                      width: 120,
                      py: 1,
                      textTransform: 'none',
                      fontWeight: 500,
                      borderRadius: 1,
                      borderColor: 'primary.main',
                      '&:hover': {
                        borderColor: 'primary.dark',
                        backgroundColor: 'rgba(25, 118, 210, 0.04)'
                      }
                    }}
                  >
                    {t('userCenter.profileSection.bindPhone')}
                  </Button>
                )}
              </Stack>
            </Box>
          )}

          {/* 语言偏好 */}
          <Box sx={{ mb: 4 }}>
            <Typography 
              variant="subtitle1" 
              sx={{ 
                mb: 2,
                fontSize: '1rem',
                fontWeight: 500,
                color: 'rgba(0, 0, 0, 0.7)'
              }}
            >
              {t('userCenter.profileSection.greeting')}
            </Typography>
            <Box sx={{ position: 'relative', maxWidth: 300 }}>
              <Button
                onClick={handleLanguageClick}
                endIcon={<KeyboardArrowDownIcon />}
                sx={{
                  width: '100%',
                  justifyContent: 'space-between',
                  color: 'text.primary',
                  textTransform: 'none',
                  border: '1px solid rgba(0, 0, 0, 0.23)',
                  borderRadius: 1,
                  py: 1,
                  px: 2,
                  fontSize: '0.95rem',
                  '&:hover': {
                    backgroundColor: 'rgba(25, 118, 210, 0.04)',
                    borderColor: 'primary.main',
                  },
                }}
              >
                {i18n.language === 'zh' ? '中文' : 'English'}
              </Button>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleLanguageClose}
                sx={{
                  '& .MuiPaper-root': {
                    minWidth: '300px',
                    boxShadow: '0px 2px 8px rgba(0,0,0,0.15)',
                    mt: 1,
                  },
                }}
              >
                <MenuItem 
                  onClick={() => handleLanguageChange("zh")}
                  selected={i18n.language === 'zh'}
                  sx={{ 
                    py: 1.5,
                    fontSize: '0.95rem',
                    '&:hover': { backgroundColor: 'rgba(25, 118, 210, 0.04)' },
                    '&.Mui-selected': {
                      backgroundColor: 'primary.light',
                      color: 'white',
                      '&:hover': {
                        backgroundColor: 'primary.main',
                      }
                    },
                  }}
                >
                  中文
                </MenuItem>
                <MenuItem 
                  onClick={() => handleLanguageChange("en")}
                  selected={i18n.language === 'en'}
                  sx={{ 
                    py: 1.5,
                    fontSize: '0.95rem',
                    '&:hover': { backgroundColor: 'rgba(25, 118, 210, 0.04)' },
                    '&.Mui-selected': {
                      backgroundColor: 'primary.light',
                      color: 'white',
                      '&:hover': {
                        backgroundColor: 'primary.main',
                      }
                    },
                  }}
                >
                  English
                </MenuItem>
              </Menu>
            </Box>
          </Box>
        </Grid>
      </Grid>

      {/* 绑定对话框 */}
      <Dialog 
        open={openBindDialog} 
        onClose={handleCloseDialog}
        maxWidth="xs"
        PaperProps={{
          sx: {
            width: '100%',
            maxWidth: '400px',
            '& .MuiDialogContent-root': {
              overflowY: 'visible'
            }
          }
        }}
      >
        <DialogTitle sx={{ pb: 1 }}>
          {bindType === 'email' 
            ? t('userCenter.profileSection.bindEmailTitle')
            : t('userCenter.profileSection.bindPhoneTitle')
          }
          <IconButton
            onClick={handleCloseDialog}
            sx={{ position: 'absolute', right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent sx={{ px: 3, py: 2 }}>
          <Box>
            <Typography variant="body2" sx={{ mb: 2 }}>
              {bindType === 'email' 
                ? t('userCenter.profileSection.verifyEmailTip')
                : t('userCenter.profileSection.verifyPhoneTip')
              }
            </Typography>
            <Typography variant="body2" sx={{ mb: 2, fontWeight: 'bold' }}>
              {bindType === 'email' 
                ? formatEmail(bindValue)
                : formatPhoneNumber(bindValue)
              }
            </Typography>
            <Stack direction="row" spacing={2} sx={{ mb: 1 }}>
              <TextField
                fullWidth
                size="small"
                placeholder={t('userCenter.profileSection.enterCode')}
                value={verificationCode}
                onChange={handleVerificationCodeChange}
                error={Boolean(verificationCode && verificationCode.length !== 6)}
                inputProps={{ 
                  maxLength: 6,
                  pattern: '[0-9]*'
                }}
              />
              <Button
                variant="outlined"
                size="small"
                disabled={countdown > 0}
                onClick={handleResendCode}
                sx={{ 
                  whiteSpace: 'nowrap',
                  minWidth: '120px'
                }}
              >
                {countdown > 0 
                  ? `${countdown}s ${t('userCenter.profileSection.resendLater')}`
                  : t('userCenter.profileSection.sendCode')
                }
              </Button>
            </Stack>
          </Box>
        </DialogContent>
        <DialogActions sx={{ px: 3, py: 2 }}>
          <Button size="small" onClick={handleCloseDialog}>
            {t('userCenter.profileSection.cancel')}
          </Button>
          <Button 
            variant="contained"
            size="small"
            onClick={handleVerify}
            disabled={!verificationCode || verificationCode.length !== 6}
          >
            {t('userCenter.profileSection.verify')}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default UserProfile; 