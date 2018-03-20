import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Select, Icon, Button, Radio, Dropdown, Menu, InputNumber, DatePicker, Modal, message, Badge, Divider, Table } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import { getClientDetail } from '../../services/client';
import { getQuoteRecordList } from '../../services/record';

import styles from './ClientDetail.less';

const FormItem = Form.Item;
const { Option } = Select;
const recordColumns = [
    {
        title: '车牌号',
        dataIndex: 'licenseNumber',
        key: 'licenseNumber',
    },
    {
        title: '车主姓名',
        dataIndex: 'ownerName',
        key: 'ownerName',
    },
    {
        title: '报价时间',
        dataIndex: 'time',
        key: 'time',
    },
    {
        title: '报价编号',
        dataIndex: 'orderCode',
        key: 'orderCode',
    }
];
const orderColumns = [
    {
        title: '订单时间',
        dataIndex: 'time',
    }, {
        title: '订单编号',
        dataIndex: 'orderCode',
    }, {
        title: '订单总额(元)',
        dataIndex: 'total',
    },
    {
        title: '支付状态',
        dataIndex: 'status',
    },
    {
        title: '返现金额(元)',
        dataIndex: 'returnMoney',
    },
    {
        title: '是否返现',
        dataIndex: 'isReturn',
    },
    {
        title: '返现时间',
        dataIndex: 'returnTime',
    },
    {
        title: '付款时间',
        dataIndex: 'payTime',
    },
    {
        title: '运单编号',
        dataIndex: 'kuaidiCode',
    }
];

const WaybillDetail = Form.create()((props) => {
    const { modalVisible, form, handleAdd, handleModalVisible } = props;
    const okHandle = () => {
        form.validateFields((err, fieldsValue) => {
            if (err) return;
            form.resetFields();
            handleAdd(fieldsValue);
        });
    };
    return (
        <Modal
            title="运单详细"
            visible={modalVisible}
            onCancel={() => handleModalVisible()}
        >
            <Row ><Col span={12} offset={6}>收件人姓名：臭傻逼</Col></Row>
            <Row ><Col span={12} offset={6}>1231231231</Col></Row>
            <Row ><Col span={12} offset={6}>收件地址：南京市玄武区仙岭大道1号苏宁青创园爱打飞机啊动法</Col></Row>
            <Row ><Col span={12} offset={6}>快递公司：顺丰速运</Col></Row>
            <Row ><Col span={12} offset={6}>运单编号：8989798</Col></Row>
            <Row ><Col span={12} offset={6}>备注：内件：（1）工具包，吸尘器，充气泵</Col></Row>
        </Modal>
    );
});

export default class ClientDetail extends PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            clientId : props.match.params.id,
            waybillModalVisible: false,
            orderModalVisible: false,
            expandForm: false,
            formValues: {},
            type: '0', // 0 未投保 1 已投保
            clientInfo: {
                name: '',
                phone: '',
                IDCard: '',
                carNumber: '',
                limitDate: '',
                source: ''
            },
            recordData: [],
            orderList: [{
                orderCode: '2341234123412341234',
                carNumber: '苏A12345',
                time: '2018-01-20',
                form: '天上',
                status: '完成',
                returnMoney: '200',
                isReturn: '是',
                returnTime: '2016-40-30',
            }]
        };
    }
    
    componentDidMount() {
        this.queryClientDetail();
        this.getQuoteRecordList();
        this.getOrderList();
    }

    queryClientDetail = () => {
        getClientDetail({
            clientId: this.state.clientId
        }).then(res => {
            if (res.code == 0) {
                this.setState({
                    clientInfo: res.data
                });
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error( '系统出错，请重试');
            console.log(err);
        })
    }

    getQuoteRecordList = () => {
        getQuoteRecordList({
            page: 1,
            number: 99,
            clientId: this.state.clientId
        }).then(res => {
            if (res.code == 0) {
                this.setState({
                    recordData: res.data
                })
            } else if (res.code == 101) {
                this.setState({
                    recordData: []
                });
            } else {
                message.error(res.msg);
            }
            console.log(res);
        }).catch(err => {
            console.log(err);
        })
    }

    getOrderList = () => {

    }

    handleWaybillModalVisible = (flag) => {
        this.setState({
            waybillModalVisible: !!flag,
        });
    }

    render() {
        const waybillModalVisible = this.state.waybillModalVisible
        const parentMethods = {
            handleModalVisible: this.handleModalVisible,
        };
        return (
            <PageHeaderLayout >
                <Card title="客户信息" className={styles.card} bordered={false}>
                    <Form layout="vertical" hideRequiredMark>
                        <Row gutter={16}>
                            <Col lg={6} md={12} sm={24}>
                                <Form.Item label='姓名'>
                                    {this.state.clientInfo.ownerName || '暂无信息'}
                                </Form.Item>
                            </Col>
                            <Col xl={{ span: 6, offset: 2 }} lg={{ span: 8 }} md={{ span: 12 }} sm={24}>
                                <Form.Item label='联系电话'>
                                {this.state.clientInfo.phone || '暂无信息'}
                                </Form.Item>
                            </Col>
                            <Col xl={{ span: 8, offset: 2 }} lg={{ span: 10 }} md={{ span: 24 }} sm={24}>
                                <Form.Item label='身份证号码'>
                                    {this.state.clientInfo.idCard || '暂无信息'}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col lg={6} md={12} sm={24}>
                                <Form.Item label='车牌号'>
                                {this.state.clientInfo.licenseNumber || '暂无信息'}
                </Form.Item>
                            </Col>
                            <Col xl={{ span: 6, offset: 2 }} lg={{ span: 8 }} md={{ span: 12 }} sm={24}>
                                <Form.Item label='保险到期'>
                                {moment(this.state.clientInfo.insuranceDeadline).format('YYYY-MM-DD') || '暂无信息'}
                </Form.Item>
                            </Col>
                            <Col xl={{ span: 8, offset: 2 }} lg={{ span: 10 }} md={{ span: 24 }} sm={24}>
                                <Form.Item label='来源渠道'>
                                {this.state.clientInfo.source || '暂无信息'}
                </Form.Item>
                            </Col>
                        </Row>
                    </Form>
                </Card>
                <Card title="报价记录" className={styles.card} bordered={false}>
                    <Table
                        // loading={loading}
                        dataSource={this.state.recordData}
                        columns={recordColumns}
                        pagination={false}
                    />
                </Card>

                <Card title="车险订单" className={styles.card} bordered={false}>
                    <Table
                        // loading={loading}
                        dataSource={this.state.orderList}
                        columns={orderColumns}
                    />
                </Card>
                <WaybillDetail
                    {...parentMethods}
                    modalVisible={waybillModalVisible}
                />

            </PageHeaderLayout >
        );
    }
}
