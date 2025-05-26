import React, { useEffect, useState } from 'react';
import { listMyChartByPageUsingPost } from '@/services/rdbi/chartController';
import {Avatar, Button, Card, Divider, List, message, Result} from 'antd';
import ReactECharts from 'echarts-for-react';
import { useModel } from '@@/exports';
import Search from "antd/es/input/Search";

/**
 * 我的图表页面
 * */
const MyChartPage: React.FC = () => {
  const initSearchParams = {
    current: 1,
    pageSize: 4,
    sortField: 'createTime',
    sortOrder: 'desc'
  };
  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
    ...initSearchParams,
  });
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const [chartList, setChartList] = useState<API.Chart[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listMyChartByPageUsingPost(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
        // 隐藏图表的title
        if (res.data.records) {
          res.data.records.forEach(data => {
            if (data.status === 'succeed'){
              const chartOption = JSON.parse(data.genChart ?? '{}');
              chartOption.title = undefined;
              data.genChart = JSON.stringify(chartOption);
            }
          })
        }
      } else {
        message.error('获取我的图表失败');
      }
    } catch (e: any) {
      message.error('获取我的图表失败' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  return (
    <div className="my-chart-page">
      <div>
        <Search
          placeholder="请输入图表名称"
          enterButton
          loading={loading}
          onSearch={(value) => {
            // 设置搜索条件
            setSearchParams({
              ...initSearchParams,
              name: value,
            });
          }}
        />
      </div>
      <div style={{ marginBottom: 16 }} />
      <List
        grid={{
          gutter: 16,
          xs: 1,
          sm: 1,
          md: 1,
          lg: 1,
          xl: 2,
          xxl: 2,
        }}
        pagination={{
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
        }}
        loading={loading}
        dataSource={chartList}
        renderItem={(item) => (
          <List.Item key={item.id}>
            <Card>
              <List.Item.Meta
                avatar={<Avatar src={currentUser && currentUser.userAvatar} />}
                title={item.name}
                description={item.chartType ? '图表类型' + item.chartType : undefined}
              />
              <>
                {item.status === 'wait' && (
                  <>
                    <Result
                      status="warning"
                      title="图表待生成"
                      subTitle={item.execMessage ?? '当前图表生成队列繁忙，请耐心等候'}
                      extra={[
                        <Button type="primary" key="console">
                          Go Console
                        </Button>,
                        <Button key="buy">Buy Again</Button>,
                      ]}
                    />
                  </>
                )}
                {item.status === 'succeed' && (
                  <>
                    <div style={{ marginBottom: 16 }} />
                    <p>{'分析目标: ' + item.goal}</p>
                    <div style={{ marginBottom: 16 }} />
                    <p>{'分析结论: ' + item.genResult}</p>
                    <ReactECharts option={item.genChart ? JSON.parse(item.genChart) : {}} />
                  </>
                )}
                {item.status === 'failed' && (
                  <>
                    <Result
                      status="error"
                      title="图表生成错误"
                      subTitle={item.execMessage}
                      extra={[
                        <Button type="primary" key="console">
                          Go Console
                        </Button>,
                        <Button key="buy">Buy Again</Button>,
                      ]}
                    />
                  </>
                )}
                {item.status === 'running' && (
                  <>
                    <Result
                      status="info"
                      title="图表生成中"
                      subTitle={item.execMessage}
                      extra={[
                        <Button type="primary" key="console">
                          Go Console
                        </Button>,
                        <Button key="buy">Buy Again</Button>,
                      ]}
                    />
                  </>
                )}
              </>
            </Card>
          </List.Item>
        )}
      />
      总数：{total}
    </div>
  );
};
export default MyChartPage;
