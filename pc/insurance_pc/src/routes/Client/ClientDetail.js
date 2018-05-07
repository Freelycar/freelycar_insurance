import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Select, Icon, Button, Radio, Dropdown, Menu, InputNumber, DatePicker, Modal, message, Badge, Divider, Table } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import { getClientDetail } from '../../services/client';
import { getQuoteRecordList, getClientOrderByLicenseNumber } from '../../services/record';

import styles from './ClientDetail.less';

const FormItem = Form.Item;
const { Option } = Select;


const WaybillDetail = Form.create()((props) => {
    const { modalVisible, form, handleModalVisible } = props;
    return (
        <Modal
            title="运单详细"
            visible={modalVisible}
            onCancel={() => handleModalVisible()}
            onOk={() => handleModalVisible()}
        >
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>收件人姓名：{form.reciver}</Col></Row>
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>收件人手机: {form.phone}</Col></Row>
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>收件地址：{form.provincesCities}{form.adressDetail}</Col></Row>
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>快递公司：{form.expressCompany}</Col></Row>
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>运单编号：{form.expressNumber}</Col></Row>
            <Row style={{ marginBottom: '15px' }}><Col span={12} offset={6}>备注：{form.remark}</Col></Row>
        </Modal>
    );
});

const InsuranceDetail = Form.create()((props) => {
    const { modalVisible, form, handleModalVisible } = props;
    let dataAry = [];
    if (form.qiangzhiList) {
        form.qiangzhiList.map((item, index) => {
            let insurance = {
                offerId: form.offerId,
                type: '强制险',
                insuranceStartTime: moment(form.insuranceStartTime * 1000).format('YYYY-MM-DD hh:mm'),
                insuranceName: item.insuranceName || '',
                insurancePrice: item.insurancePrice || '',
                totalPrice: form.totalPrice || '',
                pay: form.totalPrice || ''
            }
            dataAry.push(insurance);
        })
    }
    if (form.shangyeList) {
        form.shangyeList.map((item, index) => {
            let insurance = {
                offerId: form.offerId,
                type: '商业险',
                insuranceStartTime: moment(form.insuranceStartTime * 1000).format('YYYY-MM-DD hh:mm'),
                insuranceName: item.insuranceName || '',
                insurancePrice: item.insurancePrice || '',
                totalPrice: form.totalPrice || '',
                pay: form.totalPrice || ''
            }
            dataAry.push(insurance);
        })
    }
    const renderContent = (value, row, index) => {
        const obj = {
            children: value,
            props: {},
        };
        // if (form.qiangzhiList && form.shangyeList) {
            
        // }
        if (index == 0) {
            obj.props.rowSpan = form.qiangzhiList.length;
        } else if ( index < form.qiangzhiList.length) {
            obj.props.rowSpan = 0;
        } else if (index == form.qiangzhiList.length) {
            obj.props.rowSpan = form.shangyeList.length;
        } else {
            obj.props.rowSpan = 0;
        }
        return obj;
    };

    const allRenderContent = (value, row, index) => {
        const obj = {
            children: value,
            props: {},
        };
        if (index == 0) {
            obj.props.rowSpan = dataAry.length
        } else {
            obj.props.rowSpan = 0
        }
        return obj;
    };

    const columns = [{
        title: '订单编号',
        dataIndex: 'offerId',
        render: allRenderContent
    }, {
        title: '险种',
        dataIndex: 'type',
        render: renderContent,
    }, {
        title: '起保日期',
        dataIndex: 'insuranceStartTime',
        render: renderContent,
    }, {
        title: '险种明细',
        dataIndex: 'insuranceName',
    }, {
        title: '金额(元)',
        dataIndex: 'insurancePrice',
    },{
        title: '合计(元)',
        dataIndex: 'totalPrice',
        render: allRenderContent
    },{
        title: '实付(元)',
        dataIndex: 'pay',
        render: allRenderContent
    }];
   
    return (
        <Modal
            title="投保明细"
            visible={modalVisible}
            onCancel={() => handleModalVisible()}
            onOk={() => handleModalVisible()}
            width={800}
        >
            <Table columns={columns} dataSource={dataAry} pagination={false} bordered />
        </Modal>
    );
});

export default class ClientDetail extends PureComponent {

    constructor(props) {
        super(props);
        this.state = {
            clientId: props.match.params.id,
            waybillModalVisible: false,
            insuranceDetailModalVisible: false,
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
            }],
            waybillData: {},
            insuranceDetailData: {}
        };
    }

    componentDidMount() {
        this.queryClientDetail();
        this.getQuoteRecordList();
        this.getClientOrderByLicenseNumber(1);
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
                message.destroy();
                message.error(res.msg);
            }
        }).catch(err => {
            message.destroy();
            message.error('系统出错，请重试');
            console.log(err);
        })
    }

    getQuoteRecordList = () => {
        getQuoteRecordList({
            page: 1,
            number: 3,
            clientId: this.state.clientId
        }).then(res => {
            if (res.code == 0) {
                res.data.map((item, index) => {
                    res.data[index].key = index;
                })
                this.setState({
                    recordData: res.data
                })
            } else if (res.code == 101) {
                this.setState({
                    recordData: []
                });
            } else {
                message.destroy();
                message.error(res.msg);
            }
            console.log(res);
        }).catch(err => {
            console.log(err);
        })
    }

    getClientOrderByLicenseNumber = (page) => {
        getClientOrderByLicenseNumber({
            licenseNumber: '苏A4PZ99',
            page: 1,
            number: 5
        }).then(res => {
            console.log(res);
            if (res && res.code === 0) {
                this.setState({
                    orderList: res.data
                })
            }
        }).catch(err => {
            console.log(res);
            message.destroy();
            message.error('请求失败');
        })
    }

    handleWaybillModalVisible = (flag) => {
        this.setState({
            waybillModalVisible: !!flag,
        });
        if (!flag) {
            this.setState({
                waybillData: {}
            })
        }
    }

    handleInsuranceDetailModalVisible = (flag) => {
        this.setState({
            insuranceDetailModalVisible: !!flag,
        });
        if (!flag) {
            this.setState({
                insuranceDetailData: {}
            })
        }
    }

    render() {
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
                dataIndex: 'createTime',
                render: val => {
                    return moment(val).format('YYYY-MM-DD hh:mm');
                }
            },
            {
                title: '报价编号',
                dataIndex: 'offerId',
                key: 'offerId',
            }
        ];
        const orderColumns = [
            {
                title: '订单时间',
                dataIndex: 'createTime',
                render: val => {
                    return moment(val * 1000).format('YYYY-MM-DD hh:mm');
                }
            }, {
                title: '订单编号',
                dataIndex: 'orderId',
                render: (text, record, index) => {
                    if (record.quoteRecord) {
                        return <a onClick={() => {
                            this.setState({
                                insuranceDetailData: record.quoteRecord,
                                insuranceDetailModalVisible: true
                            })
                        }} >{text}</a>
                    } else {
                        return <a >{text}</a>
                    }
                }
            }, {
                title: '订单总额(元)',
                dataIndex: 'totalPrice',
            },
            {
                title: '订单状态',
                dataIndex: 'stateString',
            },
            {
                title: '返现金额(元)',
                dataIndex: 'backmoney',
            },
            {
                title: '是否返现',
                dataIndex: 'cashback',
                render: val => {
                    return val ? '是' : '否'
                }
            },
            {
                title: '返现时间',
                dataIndex: 'cashbackTime',
                render: val => {
                    return moment(val * 1000).format('YYYY-MM-DD hh:mm');
                }
            },
            {
                title: '付款时间',
                dataIndex: 'payTime',
                render: val => {
                    return moment(val * 1000).format('YYYY-MM-DD hh:mm');
                }
            },
            {
                title: '运单编号',
                dataIndex: 'expressNumber',
                render: (text, record, index) => {
                    if (record.expressNumber && record.reciver) {
                        return <a onClick={() => {
                            this.setState({
                                waybillData: record.reciver,
                                waybillModalVisible: true
                            })
                        }} >{record.expressNumber}</a>
                    } else {
                        return ''
                    }
                }
            }
        ];
        const waybillModalVisible = this.state.waybillModalVisible;
        const insuranceDetailModalVisible = this.state.insuranceDetailModalVisible;
        const parentMethods = {
            form: this.state.waybillData,
            handleModalVisible: this.handleWaybillModalVisible,
        };
        const insuranceParentMethods = {
            form: this.state.insuranceDetailData,
            handleModalVisible: this.handleInsuranceDetailModalVisible,
        }
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
                                    {this.state.clientInfo.insuranceDeadline ? moment(this.state.clientInfo.insuranceDeadline).format('YYYY-MM-DD') : '暂无信息'}
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
                        pagination={false}
                    />
                </Card>
                <WaybillDetail
                    {...parentMethods}
                    modalVisible={waybillModalVisible}
                />
                <InsuranceDetail
                    {...insuranceParentMethods}
                    modalVisible={insuranceDetailModalVisible}
                />

            </PageHeaderLayout >
        );
    }
}
