import React from 'react';
import { 
  Box, 
  Typography, 
  Paper,
  Divider,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination
} from '@mui/material';
import { useTranslation } from 'react-i18next';

function PointsInfo() {
  const { t } = useTranslation();
  const [page, setPage] = React.useState(0);
  const rowsPerPage = 10;

  // 模拟积分明细数据
  const pointsRecords = [
    { 
      id: 1, 
      date: '2024-03-20', 
      type: '任务奖励', 
      points: '+100', 
      description: '完成日常任务',
      balance: 1000
    },
    { 
      id: 2, 
      date: '2024-03-19', 
      type: '积分兑换', 
      points: '-200', 
      description: '兑换商品',
      balance: 800
    },
    { 
      id: 3, 
      date: '2024-03-18', 
      type: '签到奖励', 
      points: '+50', 
      description: '每日签到',
      balance: 1000
    },
    { 
      id: 4, 
      date: '2024-03-17', 
      type: '任务奖励', 
      points: '+150', 
      description: '完成周常任务',
      balance: 950
    },
    { 
      id: 5, 
      date: '2024-03-16', 
      type: '积分兑换', 
      points: '-300', 
      description: '兑换优惠券',
      balance: 800
    },
    { 
      id: 6, 
      date: '2024-03-15', 
      type: '签到奖励', 
      points: '+50', 
      description: '每日签到',
      balance: 1100
    },
    { 
      id: 7, 
      date: '2024-03-14', 
      type: '任务奖励', 
      points: '+200', 
      description: '完成专项任务',
      balance: 1050
    },
    { 
      id: 8, 
      date: '2024-03-13', 
      type: '积分过期', 
      points: '-100', 
      description: '积分到期扣除',
      balance: 850
    },
    { 
      id: 9, 
      date: '2024-03-12', 
      type: '签到奖励', 
      points: '+50', 
      description: '每日签到',
      balance: 950
    },
    { 
      id: 10, 
      date: '2024-03-11', 
      type: '任务奖励', 
      points: '+100', 
      description: '完成每周任务',
      balance: 900
    }
  ];

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Box sx={{ height: 800 }}>
      {/* 积分概览 */}
      <Box 
        sx={{ 
          display: 'flex', 
          mb: 4,
          minHeight: 100,
          position: 'relative',
          '& > div': {
            flex: 1,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center'
          }
        }}
      >
        {/* 我的积分 */}
        <Box sx={{ width: '45%' }}>
          <Typography variant="body2" color="textSecondary" sx={{ mb: 1 }}>
            {t('userCenter.pointsSection.myPoints')}
          </Typography>
          <Typography variant="h4" color="primary" sx={{ fontWeight: 500 }}>
            1000
          </Typography>
        </Box>

        {/* 分隔线 */}
        <Divider 
          orientation="vertical" 
          flexItem 
          sx={{ 
            position: 'absolute',
            left: '50%',
            height: 60,
            top: '50%',
            transform: 'translate(-50%, -50%)',
          }} 
        />

        {/* 即将过期积分 */}
        <Box sx={{ width: '45%' }}>
          <Typography variant="body2" color="textSecondary" sx={{ mb: 1 }}>
            {t('userCenter.pointsSection.expiringPoints')}
          </Typography>
          <Typography variant="h4" color="error" sx={{ fontWeight: 500 }}>
            50
          </Typography>
        </Box>
      </Box>

      {/* 积分明细列表 */}
      <TableContainer 
        component={Paper} 
        sx={{ 
          height: 'auto',  // 自适应高度
          '& .MuiTable-root': {
            '& thead': {
              backgroundColor: '#fff',
            }
          }
        }}
      >
        <Table>  {/* 移除 stickyHeader */}
          <TableHead>
            <TableRow>
              <TableCell width="15%">日期</TableCell>
              <TableCell width="15%">类型</TableCell>
              <TableCell width="15%">积分变动</TableCell>
              <TableCell width="15%">变动后积分</TableCell>
              <TableCell>说明</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pointsRecords
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((record) => (
                <TableRow key={record.id}>
                  <TableCell>{record.date}</TableCell>
                  <TableCell>{record.type}</TableCell>
                  <TableCell sx={{ 
                    color: record.points.startsWith('+') ? 'success.main' : 'error.main',
                    fontWeight: 500
                  }}>
                    {record.points}
                  </TableCell>
                  <TableCell>{record.balance}</TableCell>
                  <TableCell>{record.description}</TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
        <TablePagination
          component="div"
          count={pointsRecords.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          rowsPerPageOptions={[10]}
          labelDisplayedRows={({ from, to, count }) => 
            `${from}-${to} 共 ${count} 条`
          }
        />
      </TableContainer>
    </Box>
  );
}

export default PointsInfo; 