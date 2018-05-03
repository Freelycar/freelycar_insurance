import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Select, Icon, Button, Radio, Dropdown, Menu, InputNumber, DatePicker, Modal, message, Badge, Divider, Table } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import { getClientList, getClientDetail } from '../../services/client';
import { Link } from 'dva/router';

import styles from './ClientList.less';

const FormItem = Form.Item;
const { Option } = Select;
const getValue = obj => Object.keys(obj).map(key => obj[key]).join(',');
const columns = [
    {
        title: '序号',
        // dataIndex: 'key',
        render: (text, record, index) => {
            return index + 1;
        }
    },
    {
        title: '车牌号码',
        dataIndex: 'licenseNumber',
        render: (text, record) => {
            return <Link to={`/client/detail/${record.id}`} >
                {text}
            </Link>
        }
    },
    {
        title: '车主姓名',
        dataIndex: 'ownerName',
    },
    {
        title: '手机号码',
        dataIndex: 'phone',
    },
    {
        title: '保险到期',
        dataIndex: 'insuranceDeadline',
        render: val => val ? <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span> : '',
    },
    {
        title: '报价时间',
        dataIndex: 'leastQuoteTime',
        render: val => val ? <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span> : '',
    },
    {
        title: '报价状态',
        dataIndex: 'quoteStateCode',
        render: val => {
            switch (val) {
                case 0:
                    return '核保中';
                case 1:
                    return '核保失败';
                case 2:
                    return '未支付';
                case 3:
                    return '承保中';
                case 4:
                    return '承保失败';
                case 5:
                    return '待配送';
                case 6:
                    return '待签收';
                case 7:
                    return '已投保';
            }
        }
    },
    {
        title: '来源渠道',
        dataIndex: 'source',
    }
];
const alreadyColumns = [
    {
        title: '序号',
        render: (text, record, index) => {
            return index + 1;
        }
    },
    {
        title: '车牌号码',
        dataIndex: 'licenseNumber',
        render: (text, record) => {
            return <Link to='/client/detail' >
                {text}
            </Link>
        }
    },
    {
        title: '车主姓名',
        dataIndex: 'ownerName',
    },
    {
        title: '手机号码',
        dataIndex: 'phone',
    },
    {
        title: '订单编号',
        dataIndex: 'limitDate',
    },
    {
        title: '订单时间',
        dataIndex: 'time',
    },
    {
        title: '订单状态',
        dataIndex: 'quoteStateCode',
        render: val => {
            switch (val) {
                case 0:
                    return '核保中';
                case 1:
                    return '核保失败';
                case 2:
                    return '未支付';
                case 3:
                    return '承保中';
                case 4:
                    return '承保失败';
                case 5:
                    return '待配送';
                case 6:
                    return '待签收';
                case 7:
                    return '已投保';
            }
        }
    },
    {
        title: '是否返现',
        dataIndex: 'cashback',
        render: val => {
            if (val) {
                return '已返现';
            } else {
                return '未返现';
            }
        }
    },
    {
        title: '返现金额',
        dataIndex: 'backmoney',
    },
    {
        title: '操作',
        // dataIndex: 'from', 
        render: val => {
            return <div>
                <a>配送</a>
                <a style={{ marginLeft: 10 }} >返现</a>
            </div>
        }
    }
];

// @connect(({ rule, loading }) => ({
//     rule,
//     loading: loading.models.rule,
// }))
@Form.create()
export default class ClientList extends PureComponent {
    state = {
        expandForm: false,
        formValues: {},
        type: '0', // 0 未投保 1 已投保,
        quotingData: [],
        quotedData: [],
        pagination: { total: 0 },
    };

    componentDidMount() {
        this.getClientList({
            toubao: false
        }, 1);
    }

    //封装请求数据
    getQueryData = (values) => {
        let queryData = { ...values };
        this.state.type == '0' ? queryData.toubao = false : queryData.toubao = true;
        if (values.cashback) {
            values.cashback == '0' ? queryData.cashback = false : queryData.cashback = true;
        }
        return queryData;
    }


    getClientList = (values, page) => {
        let queryData = this.getQueryData(values)
        queryData.page = page;
        queryData.number = 10;
        if (page == 1) {
            this.setState({
                pagination: { total: 0, current: 1 }
            })
        }
        getClientList({
            ...queryData
        }).then(res => {
            console.log('*************');
            console.log(res);
            if (res && res.code == 0) {
                console.log('*************');
                console.log('i am here')
                res.data.map((item, index) => {
                    res.data[index].key = item.id
                })
                this.setState({
                    quotingData: res.data,
                    pagination: { total: res.counts }
                })
            }
        }).catch(err => {

        })
    }

    handleFormReset = () => {
        const { form } = this.props;
        form.resetFields();
        this.setState({
            formValues: {},
        });
    }


    //查询
    handleSearch = (e) => {
        e.preventDefault();

        const { form } = this.props;

        form.validateFields((err, fieldsValue) => {
            if (err) return;
            console.log(fieldsValue);
            const values = {
                ...fieldsValue,
                updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
            };

            this.getClientList(values, 1);
        });
    }


    //动态获得头部筛选字段
    renderSimpleForm() {
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSearch} layout="inline">
                <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                    <Col md={6} sm={24}>
                        <FormItem label="车牌号码">
                            {getFieldDecorator('licenseNumber')(
                                <Input placeholder="请输入" />
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="订单状态">
                            {getFieldDecorator('quoteStateCode')(
                                <Select placeholder="请选择" style={{ width: '100%' }}>
                                    <Option value="5">待配送</Option>
                                    <Option value="6">待签收</Option>
                                    <Option value="7">已投保</Option>
                                </Select>
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="是否返现">
                            {getFieldDecorator('cashback')(
                                <Select placeholder="请选择" style={{ width: '100%' }}>
                                    <Option value='0'>否</Option>
                                    <Option value='1'>是</Option>
                                </Select>
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <span className={styles.submitButtons}>
                            <Button type="primary" htmlType="submit">查询</Button>
                            <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>重置</Button>
                        </span>
                    </Col>
                </Row>
            </Form>
        );
    }

    renderAdvancedForm() {
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSearch} layout="inline">
                <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                    <Col md={6} sm={24}>
                        <FormItem label="车牌号码">
                            {getFieldDecorator('licenseNumber')(
                                <Input placeholder="请输入" />
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="手机号码">
                            {getFieldDecorator('phone')(
                                <Input placeholder="请输入" />
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="来源渠道">
                            {getFieldDecorator('source')(
                                <Input placeholder="请输入" />
                            )}
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="报价状态">
                            {getFieldDecorator('quoteStateCode')(
                                <Select placeholder="请选择" style={{ width: '100%' }}>
                                    <Option value="0">核保中</Option>
                                    <Option value="1">核保失败</Option>
                                    <Option value="2">未支付</Option>
                                    <Option value="3">承保中</Option>
                                    <Option value="4">承保失败</Option>
                                </Select>
                            )}
                        </FormItem>
                    </Col>
                </Row>
                <div style={{ overflow: 'hidden' }}>
                    <span style={{ float: 'right', marginBottom: 24 }}>
                        <Button type="primary" htmlType="submit">查询</Button>
                        <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>重置</Button>
                    </span>
                </div>
            </Form>
        );
    }

    renderForm() {
        return this.state.type === '0' ? this.renderAdvancedForm() : this.renderSimpleForm();
    }

    handleTypeChange = (e) => {
        this.setState({ type: e.target.value }, () => this.getClientList({}, 1));
    }

    handleTableChange = (pagination) => {
        const pager = { ...this.state.pagination };
        pager.current = pagination.current;
        this.setState({
            pagination: pager
        })
        console.log(pagination)
        this.getClientList({}, pagination.current)
    }

    exprotExcel = () => {   //导出excel  TODO

    }

    render() {
        const { type } = this.state;

        let tableColumns;
        if (type === '0') {
            tableColumns = columns;
        } else {
            tableColumns = alreadyColumns;
        }

        return (
            <PageHeaderLayout title="车险客户">
                <Card bordered={false}>
                    <div className={styles.tableList}>
                        <div className={styles.tableListOperator}>
                            <Radio.Group value={this.state.type} onChange={this.handleTypeChange} size='large' type="primary">
                                <Radio.Button value='0' >未投保</Radio.Button>
                                <Radio.Button value='1'>已投保</Radio.Button>
                            </Radio.Group>
                        </div>
                        <div className={styles.tableListForm}>
                            {this.renderForm()}
                        </div>
                        <div className={styles.tableListOperator}>
                            <Button type="primary" icon="download" size='large' onClick={this.exprotExcel} >导出</Button>
                        </div>
                        <Table
                            // loading={loading}
                            dataSource={this.state.type == '0' ? this.state.quotingData : this.state.quotedData}
                            columns={tableColumns}
                            onChange={(pagination) => this.handleTableChange(pagination)}
                            pagination={this.state.pagination}
                        />
                    </div>
                </Card>
            </PageHeaderLayout >
        );
    }
}
