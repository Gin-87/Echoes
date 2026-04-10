import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Button,
  Box,
  Typography,
  TextField,
  IconButton,
  InputAdornment,
  Divider,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import { useToast } from '../General/Business/ToastContext';
import { getPublicKey, register, encryptPassword, login, getUserPreference } from '../../../services/authService';
import request from '../../../utils/request';

const LoginDialog = ({ open, onClose, onSuccess }) => {
  const { t, i18n } = useTranslation();
  const { showToast } = useToast();
  const [mode, setMode] = useState('login'); // login, register
  const [step, setStep] = useState('credentials'); // credentials, region, mainland, overseas
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    account: '',  // 手机号或邮箱
    password: '',
    confirmPassword: '', // 添加确认密码字段
    verificationCode: '',
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // 从 localStorage 中获取语言偏好
    const savedLanguage = localStorage.getItem('languagePrefer');
    if (savedLanguage) {
      i18n.changeLanguage(savedLanguage); // 设置为用户之前的语言偏好
    }
  }, []);

  const resetDialog = () => {
    setMode('login');
    setStep('credentials');
    setShowPassword(false);
    setFormData({
      account: '',
      password: '',
      confirmPassword: '',
      verificationCode: '',
    });
  };

  const handleClose = () => {
    resetDialog();
    onClose();
  };

  const handleChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value
    }));
  };

  // 处理登录
  const handleLogin = async () => {
    try {
      setLoading(true);
      const encryptedPassword = await encryptPassword(formData.password);
      
      const loginData = {
        identifier: formData.account,
        password: encryptedPassword
      };

      const response = await login(loginData);

      if (response.code === 200) {
        // 登录成功后获取用户偏好
        try {
          const preference = await getUserPreference();
          const userLanguage = preference?.languagePrefer?.toLowerCase();

          // 尝试使用本地缓存的语言偏好
          const cachedLanguage = localStorage.getItem('languagePrefer')?.toLowerCase() || 'zh';

          // 处理语言偏好
          if (userLanguage === 'chinese') {
            i18n.changeLanguage('zh'); // 设置为中文
            localStorage.setItem('languagePrefer', 'zh'); // 更新缓存
          } else if (userLanguage === 'english') {
            i18n.changeLanguage('en'); // 设置为英文
            localStorage.setItem('languagePrefer', 'en'); // 更新缓存
          } else if (cachedLanguage === 'chinese') {
            i18n.changeLanguage('zh'); // 使用缓存的中文
          } else if (cachedLanguage === 'english') {
            i18n.changeLanguage('en'); // 使用缓存的英文
          } else {
            i18n.changeLanguage('zh'); // 默认使用中文
            localStorage.setItem('languagePrefer', 'zh'); // 更新缓存
          }
        } catch (prefError) {
          console.error('Failed to load user preferences:', prefError);
          const cachedLanguage = localStorage.getItem('languagePrefer')?.toLowerCase() || 'zh';
          
          // 如果获取偏好失败，使用缓存的语言
          if (cachedLanguage === 'chinese') {
            i18n.changeLanguage('zh'); // 使用缓存的中文
          } else if (cachedLanguage === 'english') {
            i18n.changeLanguage('en'); // 使用缓存的英文
          } else {
            i18n.changeLanguage('zh'); // 默认使用中文
          }
          localStorage.setItem('languagePrefer', 'zh'); // 更新缓存
        }

        showToast(t("auth.loginSuccess"), "success");
        
        // 确保这些回调被执行
        if (typeof onSuccess === 'function') {
          onSuccess();
        }
        if (typeof onClose === 'function') {
          onClose();
        }

        // 触发登录成功事件
        window.dispatchEvent(new Event('loginSuccess'));
        
        // 添加页面刷新
        window.location.reload();
      } else {
        showToast(response.message || t("auth.loginFailed"), "error");
      }
    } catch (error) {
      console.error('Login error:', error);
      showToast(t("auth.loginFailed"), "error");
    } finally {
      setLoading(false);
    }
  };

  // 处理注册点击
  const handleRegisterClick = () => {
    setMode('register');
    setStep('region');
  };

  // 处理区域选择
  const handleRegionSelect = (region) => {
    setStep(region);
    setFormData(prev => ({
      ...prev,
      account: '',  // 清空账号
      password: '', // 清空密码
      verificationCode: '', // 清空验证码
    }));
  };

  // 处理发送验证码
  const handleSendCode = () => {
    if (!formData.account) {
      showToast(t('auth.phoneRequired'), 'error');
      return;
    }
    // TODO: 实现发送验证码逻辑
    showToast(t('auth.codeSent'), 'success');
  };

  // 密码验证函数
  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,18}$/;
    return passwordRegex.test(password);
  };

  // 添加手机号验证函数
  const validatePhone = (phone) => {
    // 中国大陆手机号正则表达式
    const phoneRegex = /^1[3-9]\d{9}$/;
    return phoneRegex.test(phone);
  };

  // 处理注册提交
  const handleSubmit = async () => {
    try {
      // 添加防重复提交标记
      if (isSubmitting) return;
      setIsSubmitting(true);

      // 表单验证
      if (!formData.account) {
        showToast(t('auth.accountRequired'), 'error');
        return;
      }

      // 根据区域进行不同的验证
      if (step === 'mainland') {
        if (!validatePhone(formData.account)) {
          showToast(t('auth.invalidPhone'), 'error');
          return;
        }
      } else {
        // 可以添加邮箱验证
        if (!formData.account.includes('@')) {
          showToast(t('auth.invalidEmail'), 'error');
          return;
        }
      }

      if (!formData.password) {
        showToast(t('auth.passwordRequired'), 'error');
        return;
      }
      if (!validatePassword(formData.password)) {
        showToast(t('auth.passwordInvalid'), 'error');
        return;
      }
      if (formData.password !== formData.confirmPassword) {
        showToast(t('auth.passwordMismatch'), 'error');
        return;
      }

      // 加密密码
      const encryptedPassword = await encryptPassword(formData.password);
      console.log('Encrypted password:', encryptedPassword);


      // 生成随机用户名
      const generateRandomUsername = () => {
        const randomNum = Math.floor(Math.random() * 1000000000).toString().padStart(9, '0');
        return `user${randomNum}`;
         };

      // 准备注册数据
      const registerData = {
        password: encryptedPassword,
        status: 'ACTIVE',
        ...(step === 'mainland' 
          ? { phone: formData.account }
          : { email: formData.account }
        ),
        nickname: generateRandomUsername(),
      };

      // 发送注册请求
      const response = await register(registerData);
      
      if (response.code === 200) {
        showToast(t('auth.registerSuccess'), 'success');
        
        // 注册成功后自动登录
        const loginData = {
          identifier: formData.account,
          password: encryptedPassword,
        };

        const loginResponse = await login(loginData);
        
        if (loginResponse.code === 200) {
          showToast(t('auth.loginSuccess'), 'success');
          onSuccess();
          onClose();
        } else {
          showToast(t('auth.loginFailed'), 'error');
        }
      } else {
        showToast(response.message || t('auth.registerFailed'), 'error');
      }
    } catch (error) {
      console.error('Registration error:', error);
      showToast(t('auth.registerFailed'), 'error');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLanguageChange = (language) => {
    i18n.changeLanguage(language);
    localStorage.setItem('languagePrefer', language); // 更新缓存
  };

  // 渲染登录表单
  const renderLoginForm = () => (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, py: 2 }}>
      <TextField
        label={t('auth.accountLabel')}
        placeholder={t('auth.accountPlaceholder')}
        value={formData.account}
        onChange={handleChange('account')}
        fullWidth
      />
      <TextField
        label={t('auth.password')}
        type={showPassword ? 'text' : 'password'}
        value={formData.password}
        onChange={handleChange('password')}
        fullWidth
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
        <Button
          variant="contained"
          fullWidth
          size="large"
          onClick={handleLogin}
          disabled={loading}
        >
          {loading ? t('auth.loggingIn') : t('auth.login')}
        </Button>
        <Button
          variant="outlined"
          fullWidth
          size="large"
          onClick={handleRegisterClick}
        >
          {t('auth.register')}
        </Button>
      </Box>
    </Box>
  );

  // 渲染区域选择界面
  const renderRegionSelection = () => (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, py: 2 }}>
      <Typography variant="body1" sx={{ textAlign: 'center', mb: 2 }}>
        {t('auth.selectRegion')}
      </Typography>
      <Button
        variant="outlined"
        size="large"
        onClick={() => handleRegionSelect('mainland')}
        sx={{ height: 56 }}
      >
        {t('auth.mainland')}
      </Button>
      <Button
        variant="outlined"
        size="large"
        onClick={() => handleRegionSelect('overseas')}
        sx={{ height: 56 }}
      >
        {t('auth.overseas')}
      </Button>
    </Box>
  );

  // 渲染中国大陆注册表单
  const renderMainlandForm = () => (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, py: 2 }}>
      <TextField
        label={t('auth.phone')}
        value={formData.account}
        onChange={handleChange('account')}
        error={Boolean(formData.account && !validatePhone(formData.account))}
        helperText={formData.account && !validatePhone(formData.account) ? t('auth.invalidPhone') : ''}
        fullWidth
        inputProps={{
          maxLength: 11,
          inputMode: 'numeric',
          pattern: '[0-9]*'
        }}
      />
      <TextField
        label={t('auth.password')}
        type={showPassword ? 'text' : 'password'}
        value={formData.password}
        onChange={handleChange('password')}
        fullWidth
        helperText={t('auth.passwordHelperText')}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <TextField
        label={t('auth.confirmPassword')}
        type={showPassword ? 'text' : 'password'}
        value={formData.confirmPassword}
        onChange={handleChange('confirmPassword')}
        fullWidth
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <Box sx={{ display: 'flex', gap: 1 }}>
        <TextField
          label={t('auth.verificationCode')}
          value={formData.verificationCode}
          onChange={handleChange('verificationCode')}
          fullWidth
        />
        <Button
          variant="outlined"
          onClick={handleSendCode}
          sx={{ minWidth: 120 }}
        >
          {t('auth.sendCode')}
        </Button>
      </Box>
      <Button
        variant="contained"
        size="large"
        onClick={handleSubmit}
        sx={{ mt: 2 }}
      >
        {t('auth.register')}
      </Button>
    </Box>
  );

  // 渲染海外注册表单
  const renderOverseasForm = () => (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, py: 2 }}>
      <TextField
        label={t('auth.email')}
        type="email"
        value={formData.account}
        onChange={handleChange('account')}
        error={Boolean(formData.account && !formData.account.includes('@'))}
        helperText={formData.account && !formData.account.includes('@') ? t('auth.invalidEmail') : ''}
        fullWidth
      />
      <TextField
        label={t('auth.password')}
        type={showPassword ? 'text' : 'password'}
        value={formData.password}
        onChange={handleChange('password')}
        fullWidth
        helperText={t('auth.passwordHelperText')}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <TextField
        label={t('auth.confirmPassword')}
        type={showPassword ? 'text' : 'password'}
        value={formData.confirmPassword}
        onChange={handleChange('confirmPassword')}
        fullWidth
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          ),
        }}
      />
      <Button
        variant="contained"
        size="large"
        onClick={handleSubmit}
        sx={{ mt: 2 }}
      >
        {t('auth.register')}
      </Button>
    </Box>
  );

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      hideBackdrop={true}
      maxWidth="xs"
      fullWidth
      PaperProps={{
        sx: { borderRadius: 2 }
      }}
    >
      <DialogTitle sx={{ m: 0, p: 2, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {mode === 'login' ? t('auth.login') : t('auth.register')}
        <IconButton onClick={handleClose}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <Divider />
      <DialogContent>
        {mode === 'login' && renderLoginForm()}
        {mode === 'register' && step === 'region' && renderRegionSelection()}
        {mode === 'register' && step === 'mainland' && renderMainlandForm()}
        {mode === 'register' && step === 'overseas' && renderOverseasForm()}
      </DialogContent>
    </Dialog>
  );
};

export default LoginDialog; 