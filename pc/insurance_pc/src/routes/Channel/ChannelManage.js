import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Icon, Button, InputNumber, Modal, message, Table, Popconfirm } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';


import { addChannel, removeChannels, getChannelList, modifyChannel } from '../../services/channel';

import styles from './ChannelManage.less';
const FormItem = Form.Item;
const CreateAddForm = Form.create()((props) => {
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
            title="新增渠道"
            visible={modalVisible}
            onOk={okHandle}
            onCancel={() => handleModalVisible()}
        >
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="渠道名称: "
            >
                {form.getFieldDecorator('name', {
                    rules: [{ required: true, message: '请输入渠道名称' }],
                })(
                    <Input placeholder="请输入渠道名称" />
                )}
            </FormItem>
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="邀请码: "
            >
                {form.getFieldDecorator('invcode', {
                    rules: [{ required: true, message: '请输入邀请码' }],
                })(
                    <Input placeholder="请输入邀请码" />
                )}
            </FormItem>
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="备注: "
            >
                {form.getFieldDecorator('remark', {
                    rules: [{ required: false, message: '' }],
                })(
                    <Input placeholder="请输入备注" />
                )}
            </FormItem>
        </Modal>
    );
});

export default class ChannelManage extends PureComponent {
    state = {
        modalVisible: false,
        modifyModalVisible: false,
        deleteModalShow: false,
        invcode: '',  //搜索用的邀请码
        name: '',   //搜索用的渠道名称,
        listData: [],
        modifyData: {
            name: '',
            remark: '',
            invcode: '',
            id: ''
        },    //用于修改的数据  
        selectedRowKeys: [],
        columns: [
            {
                title: '序号',
                dataIndex: 'no',
                render: (text, data, index) => {
                    return index + 1;
                }
            },
            {
                title: '渠道名称',
                dataIndex: 'name',
            },
            {
                title: '邀请码',
                dataIndex: 'invcode',
            },
            {
                title: '创建时间',
                dataIndex: 'createTime',
            },
            {
                title: '备注',
                dataIndex: 'remark',
            },
            {
                title: '操作',
                // dataIndex: 'from', 
                render: item => {
                    return <div>
                        <a onClick={() => this.showModifyModal(item)} >修改</a>
                        <Popconfirm title="是否要删除此行？" onConfirm={() => this.deleteChannel([item.id])}>
                            <a style={{ marginLeft: 20 }} >删除</a>
                        </Popconfirm>
                    </div>
                }
            }],
        pagination: { total: 0 },
    };

    showModifyModal = (item) => {
        this.setState({
            modifyData: JSON.parse(JSON.stringify(item)),
            modifyModalVisible: true
        })
    }

    componentDidMount() {
        this.getChannelList(1);
    }

    getChannelList = (page) => {
        if (page == 1) {
            this.setState({
                pagination: { total: 0, current: 1 }
            })
        }
        getChannelList({
            invcode: this.state.invcode,
            name: this.state.name,
            page: page,
            number: 10
        }).then((res) => {
            if (res.code == 0) {
                res.data.map((item, index) => {
                    item.key = item.id;
                    item.createTime = moment.unix(item.createTime / 1000).format('YYYY-MM-DD hh:mm');
                })
                this.setState({
                    listData: res.data,
                    pagination: { total: res.counts }
                })
            } else {
                this.setState({
                    listData: [],
                    pagination: { total: 0 }
                })
            }
        }).catch((error) => {
            console.log(error)
        })
    }

    handleFormReset = () => {
        this.setState({
            name: '',
            invcode: ''
        })
    }

    handleSearch = () => {
    }

    handleModalVisible = (flag) => {
        this.setState({
            modalVisible: !!flag,
        });
    }

    handleAdd = (fields) => {
        addChannel({
            ...fields
        }).then(res => {
            if (res.code == 0) {
                this.setState({
                    modalVisible: false,
                });
                message.success('添加成功');
                this.getChannelList(1)
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error('添加失败，请重试！');
            console.log(err);
        })
    }

    handleModify = () => {
        if (!this.state.modifyData.name) {
            message.warn('请输入渠道名称!');
            return;
        } else if (!this.state.modifyData.invcode) {
            message.warn('请输入邀请码!');
            return;
        }
        let modifyData = this.state.modifyData;
        delete modifyData.createTime;
        delete modifyData.key
        modifyChannel({
            ...modifyData,
        }).then(res => {
            if (res.code == 0) {
                this.setState({
                    modifyModalVisible: false,
                    modifyData: {
                        name: '',
                        remark: '',
                        invcode: '',
                        id: ''
                    }
                });
                message.success('修改成功');
                this.getChannelList(1)
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error('修改失败，请重试！');
            console.log(err);
        })
    }

    deleteChannel = (ids) => {
        if (ids.length <= 0) {
            return;
        }
        const id = ids.join(',');
        removeChannels({
            id: id
        }).then(res => {
            if (res.code == 0) {
                this.setState({deleteModalShow: false});
                message.success('删除成功');
                this.getChannelList(1);
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error('删除失败');
        })
    }

    handleInputChange = (e, key) => {
        if (key == 'name') {
            this.setState({
                name: e.target.value
            })
        } else if (key == 'invcode') {
            this.setState({
                invcode: e.target.value
            })
        }
    }

    renderSimpleForm() {
        return (
            <Form layout="inline">
                <Row gutter={{ md: 8, lg: 24, xl: 48 }}>

                    <Col md={6} sm={24}>
                        <FormItem label="业务渠道名称">
                            <Input placeholder='请输入渠道名称' value={this.state.name} onChange={(e) => { this.handleInputChange(e, "name") }} />
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="邀请码">
                            <Input placeholder='请输入邀请码' value={this.state.invcode} onChange={(e) => { this.handleInputChange(e, "invcode") }} />
                        </FormItem>
                    </Col>

                    <Col md={8} sm={24}>
                        <span className={styles.submitButtons}>
                            <Button type="primary" htmlType="submit" onClick={() => this.getChannelList(1)} >查询</Button>
                            <Button style={{ marginLeft: 30 }} onClick={this.handleFormReset}>重置</Button>
                        </span>
                    </Col>
                </Row>
            </Form>
        );
    }

    getModifyModal = () => {
        return <Modal
            title="修改渠道"
            visible={this.state.modifyModalVisible}
            onOk={this.handleModify}
            onCancel={() => { this.setState({ modifyModalVisible: false, modifyData: {} }) }}
        >
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="渠道名称: "
            >
                <Input placeholder="请输入渠道名称" value={this.state.modifyData.name || ''} onChange={(e) => { this.handleModifyData(e.target.value, 'name') }} />
            </FormItem>
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="邀请码: "
            >
                <Input placeholder="请输入邀请码" value={this.state.modifyData.invcode || ''} onChange={(e) => { this.handleModifyData(e.target.value, 'invcode') }} />
            </FormItem>
            <FormItem
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 15 }}
                label="备注: "
            >
                <Input placeholder="请输入备注" value={this.state.modifyData.remark || ''} onChange={(e) => { this.handleModifyData(e.target.value, 'remark') }} />
            </FormItem>
        </Modal>
    }

    handleModifyData = (value, key) => {
        let modifyData = this.state.modifyData;
        modifyData[key] = value;
        this.setState({
            modifyData: JSON.parse(JSON.stringify(modifyData))
        })
    }

    onSelectChange = (selectedRowKeys) => {
        console.log('selectedRowKeys changed: ', selectedRowKeys);
        this.setState({ selectedRowKeys });
    }

    handleTableChange = (pagination) => {
        const pager = { ...this.state.pagination };
        pager.current = pagination.current;
        this.setState({
            pagination: pager
        })
        this.getChannelList(pagination.current)
    }

    render() {

        const parentMethods = {
            handleAdd: this.handleAdd,
            handleModalVisible: this.handleModalVisible,
        };
        const { loading, selectedRowKeys } = this.state;
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <PageHeaderLayout title="渠道管理">
                <Card bordered={false}>
                    <div className={styles.tableList}>
                        <div className={styles.tableListForm}>
                            {this.renderSimpleForm()}
                        </div>
                        <div className={styles.tableListOperator} style={{ marginTop: 50 }}>
                            <Button onClick={() => { this.setState({ modalVisible: true }) }} >新增</Button>
                            <Button style={{ marginLeft: 20 }} onClick={() => this.setState({deleteModalShow: true})} >批量删除</Button>
                        </div>
                        <Table
                            //   loading={loading}
                            // data={data}
                            dataSource={this.state.listData}
                            columns={this.state.columns}
                            rowSelection={rowSelection}
                            onChange={(pagination) => this.handleTableChange(pagination)}
                            pagination={this.state.pagination}
                        />
                    </div>
                </Card>
                <CreateAddForm
                    {...parentMethods}
                    modalVisible={this.state.modalVisible}
                    modifyData={this.state.modifyData}
                />
                {this.getModifyModal()}
                <Modal
                    title="提示"
                    visible={this.state.deleteModalShow}
                    onOk={() => this.deleteChannel(this.state.selectedRowKeys)}
                    onCancel={() => this.setState({
                        deleteModalShow: false
                    })}
                >
                    确认批量删除选中的项目？
                </Modal>
            </PageHeaderLayout >
        );
    }
}
