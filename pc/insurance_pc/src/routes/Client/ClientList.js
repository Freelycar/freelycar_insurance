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
        title: '保险到期',
        dataIndex: 'limitDate',
        render: val => <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span>,
    },
    {
        title: '报价时间',
        dataIndex: 'time',
        render: val => <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span>,
    },
    {
        title: '报价状态',
        dataIndex: 'quoteState',
        render: val => {
            switch (val) {
                case '1':
                    return '待报价';
                case '2':
                    return '已报价';
                case '3':
                    return '核保成功';
                case '4':
                    return '核保失败';
                case '5':
                    return '待签收';
                case '6':
                    return '待配送';
                case '7':
                    return '已配送';
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
        dataIndex: 'quoteState',
        render: val => {
            switch (val) {
                case '1':
                    return '待报价';
                case '2':
                    return '已报价';
                case '3':
                    return '核保成功';
                case '4':
                    return '核保失败';
                case '5':
                    return '待签收';
                case '6':
                    return '待配送';
                case '7':
                    return '已配送';
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

const norealData = [{
    key: '1',
    licenseNumber: '苏A6666',
    ownerName: '杨威',
    phone: '15651751173',
    limitDate: '2018-03-25',
    time: '2018-03-24',
    quoteState: '3',
    source: '微信小程序'
}]

const CreateForm = Form.create()((props) => {
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
            title="新建规则"
            visible={modalVisible}
            onOk={okHandle}
            onCancel={() => handleModalVisible()}
        >
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="描述"
            >
                {form.getFieldDecorator('desc', {
                    rules: [{ required: true, message: 'Please input some description...' }],
                })(
                    <Input placeholder="请输入" />
                )}
            </FormItem>
        </Modal>
    );
});
@connect(({ rule, loading }) => ({
    rule,
    loading: loading.models.rule,
}))
@Form.create()
export default class ClientList extends PureComponent {
    state = {
        modalVisible: false,
        expandForm: false,
        formValues: {},
        type: '0', // 0 未投保 1 已投保,
        quotingData: [],
        quotedData: []
    };

    componentDidMount() {
        this.getClientList({
            page: 1,
            number: 99,
            toubao: false
        });
    }

    getQueryData = (values) => {
        let queryData = { ...values };
        this.state.type == '0' ? queryData.toubao = false : queryData.toubao = true;
        queryData.page = 1;
        queryData.number = 99;
        if (values.cashback) {
            values.cashback == '0' ? queryData.cashback = false : queryData.cashback = true;
        }
        return queryData;
    }

    getClientList = (values) => {
        let queryData = this.getQueryData(values)
        getClientList({
            ...queryData
        }).then(res => {
            console.log(res);
            if (res.code == 0) {
                if (this.state.type == '0') {
                    this.setState({
                        quotingData: res.data
                    })
                } else {
                    this.setState({
                        quotedData: res.data
                    })
                }
            }
        }).catch({

        })
    }

    handleFormReset = () => {
        const { form } = this.props;
        form.resetFields();
        this.setState({
            formValues: {},
        });
    }

    toggleForm = () => {
        this.setState({
            expandForm: !this.state.expandForm,
        });
    }

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

            this.getClientList(values);
        });
    }

    handleModalVisible = (flag) => {
        this.setState({
            modalVisible: !!flag,
        });
    }

    handleAdd = (fields) => {
        message.success('添加成功');
        this.setState({
            modalVisible: false,
        });
    }

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
                            {getFieldDecorator('quotestate')(
                                <Select placeholder="请选择" style={{ width: '100%' }}>
                                    <Option value="5">待签收</Option>
                                    <Option value="6">待配送</Option>
                                    <Option value="7">已签收</Option>
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
                            {getFieldDecorator('quotestate')(
                                <Select placeholder="请选择" style={{ width: '100%' }}>
                                    <Option value="1">待报价</Option>
                                    <Option value="2">已报价</Option>
                                    <Option value="3">核保成功</Option>
                                    <Option value="4">核保失败</Option>
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
        this.setState({ type: e.target.value });
    }

    render() {
        // const { rule: { data }, loading } = this.props;
        const { modalVisible, type } = this.state;

        const parentMethods = {
            handleAdd: this.handleAdd,
            handleModalVisible: this.handleModalVisible,
        };
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
                            <Button type="primary" icon="download" size='large'>导出</Button>
                        </div>
                        <Table
                            // loading={loading}
                            dataSource={this.state.type == '0' ? this.state.quotingData : this.state.quotedData}
                            columns={tableColumns}
                        />
                    </div>
                </Card>
                <CreateForm
                    {...parentMethods}
                    modalVisible={modalVisible}
                />
            </PageHeaderLayout >
        );
    }
}
