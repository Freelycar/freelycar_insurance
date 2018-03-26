import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Input, Icon, Button, Radio, message, Table, DatePicker } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import ReactEcharts from 'echarts-for-react';
import { getPieChart } from '../../services/order';

const { RangePicker } = DatePicker;
const Search = Input.Search;
import styles from './PerformanceStatistics.less';

const columns = [
    {
        title: '业务渠道名称',
        dataIndex: 'source',
        key: 'from',
    },
    {
        title: '销售额（元）',
        dataIndex: 'money',
        key: 'money',
    }
];

const testData = [{
    source: '杨威介绍',
    money: '23234'
}];

export default class PerformanceStatistics extends PureComponent {
    state = {
        type: '0',
        startTime: moment().format('YYYY/MM/DD'),
        endTime: moment().format('YYYY/MM/DD'),
        allData: []
    };

    componentDidMount() {
        this.getPieChart();
    }

    getPieChart = () => {
        getPieChart({
            startTime: this.state.startTime,
            endTime: this.state.endTime
        }).then(res => {
            console.log(res);
            if (res && res.code == 0) {
                this.setState({
                    allData: res.data
                })
            } else {
                message.warn(res.msg || '请求失败')
            }
        }).catch(err => {

        })
    }

    handleSearch = (e) => {
        console.log(e.target.value)
    }

    handleTypeChange = (e) => {   //0 今日  1 本月  2 区间查找
        if (type == this.state.type) {
            return;
        }
        this.setState({ type: e.target.value });
        const type = e.target.value;
        if (type == 0) {
            this.setState({
                startTime: moment().format('YYYY/MM/DD'),
                endTime: moment().format('YYYY/MM/DD')
            }, () => this.getPieChart())    
        } else if (type == 1) {
            let time = moment();
            this.setState({
                startTime: time.format('YYYY/MM/DD'),
                endTime: time.subtract(1, "months").format("YYYY/MM/DD")
            }, () => this.getPieChart()) 
        } else if (type == 2) {
            this.setState({
                startTime: '',
                endTime: ''
            })
        }
    }

    handleTimeChange = (date, dateString) => {
        console.log(date, dateString);
        this.setState({
            startTime: dateString[0].replace(/-/g, '/'),
            endTime: dateString[1].replace(/-/g, '/')
        }, () => this.getPieChart())
    }

    getOption = () => {
        // var data = genData(10);
        const data =  genData(this.state.allData);
        function genData(data) {
            // var nameList = [
            //     '丑', '穷', '搓', '矮', '糙', '短', '秃', '臭', '屌', '陈', '褚', '卫', '蒋', '沈', '韩', '杨', '朱', '秦', '尤', '许', '何', '吕', '施', '张', '孔', '曹', '严', '华', '金', '魏', '陶', '姜', '戚', '谢', '邹', '喻', '柏', '水', '窦', '章', '云', '苏', '潘', '葛', '奚', '范', '彭', '郎', '鲁', '韦', '昌', '马', '苗', '凤', '花', '方', '俞', '任', '袁', '柳', '酆', '鲍', '史', '唐', '费', '廉', '岑', '薛', '雷', '贺', '倪', '汤', '滕', '殷', '罗', '毕', '郝', '邬', '安', '常', '乐', '于', '时', '傅', '皮', '卞', '齐', '康', '伍', '余', '元', '卜', '顾', '孟', '平', '黄', '和', '穆', '萧', '尹', '姚', '邵', '湛', '汪', '祁', '毛', '禹', '狄', '米', '贝', '明', '臧', '计', '伏', '成', '戴', '谈', '宋', '茅', '庞', '熊', '纪', '舒', '屈', '项', '祝', '董', '梁', '杜', '阮', '蓝', '闵', '席', '季', '麻', '强', '贾', '路', '娄', '危'
            // ];
            var legendData = [];
            var seriesData = [];
            var selected = {};
            // for (var i = 0; i < count; i++) {
            //     name = Math.random() > 0.65
            //         ? makeWord(4, 1) + '·' + makeWord(3, 0)
            //         : makeWord(2, 1);
            //     legendData.push(name);
            //     seriesData.push({
            //         name: name,
            //         value: Math.round(Math.random() * 100000)
            //     });
            //     selected[name] = true;
            // }

            data.map((item, index) => {
                legendData.push(item.source);
                seriesData.push({
                    name: item.source,
                    value: item.price_yuan
                });
                selected[item.source] = true;
            })
            for (let i = 0; i < data.length; i++) {
                legendData.push()
            }
            console.log(legendData, seriesData, selected)
            return {
                legendData: legendData,
                seriesData: seriesData,
                selected: selected
            };

            function makeWord(max, min) {
                var nameLen = Math.ceil(Math.random() * max + min);
                var name = [];
                for (var i = 0; i < nameLen; i++) {
                    name.push(nameList[Math.round(Math.random() * nameList.length - 1)]);
                }
                return name.join('');
            }
        }
        return {
            // title: {
            //     text: '业绩统计',
            //     subtext: '纯属虚构',
            //     x: 'center'
            // },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                right: 10,
                top: 20,
                bottom: 20,
                data: data.legendData,

                selected: data.selected
            },
            series: [
                {
                    name: '姓名',
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    data: data.seriesData,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        }
    }




    render() {

        return (
            <PageHeaderLayout title="业绩统计">
                <Card bordered={false}>
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                        <Col md={6} sm={24}>
                            <Radio.Group value={this.state.type} onChange={this.handleTypeChange} size='large' type="primary">
                                <Radio.Button value='0' >今日</Radio.Button>
                                <Radio.Button value='1'>本月</Radio.Button>
                                <Radio.Button value='2'>区间查找</Radio.Button>
                            </Radio.Group>
                        </Col>
                        {this.state.type == 2 ? <Col md={6} sm={24}>
                            {/* <DatePicker onChange={this.handleTimeChange} /> */}
                            <RangePicker onChange={this.handleTimeChange} />
                        </Col> : ''}
                        
                    </Row>
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }} style={{ marginTop: 50 }}>
                        <Col md={16} sm={24}>
                            {this.state.allData.length > 0 ? <ReactEcharts
                                option={this.getOption()}
                                lazyUpdate={true}
                            />: '暂无数据'}
                        </Col>
                    </Row>
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }} style={{ marginTop: 50 }}>
                        
                        <Col md={12} sm={24}>
                            <Search
                                placeholder="输入业务渠道名称"
                                onSearch={this.handleSearch}
                                enterButton
                            />
                            <Table
                                // loading={loading}
                                dataSource={testData}
                                columns={columns}
                                style={{ marginTop: 20 }}
                            />
                        </Col>
                    </Row>
                </Card>

            </PageHeaderLayout >
        );
    }
}
