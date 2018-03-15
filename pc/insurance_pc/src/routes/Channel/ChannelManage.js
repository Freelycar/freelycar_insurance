import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Icon, Button, InputNumber, Modal, message, Table, Popconfirm } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';


import { addChannel, removeChannels, getChannelList } from '../../services/channel';

import styles from './ChannelManage.less';


const FormItem = Form.Item;

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
        invcode: '',  //搜索用的邀请码
        name: '',   //搜索用的渠道名称,
        listData: [],
        columns : [
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
                        <a>修改</a>
                        <Popconfirm title="是否要删除此行？" onConfirm={() => this.deleteChannel([item.id])}>
                            <a style={{marginLeft : 20}} >删除</a>
                        </Popconfirm>
                    </div>
                }
            }]
    };

    componentDidMount() {
        this.getChannelList();
    }

    getChannelList = () => {
        getChannelList({
            invcode: this.state.invcode,
            name: this.state.name,
            page: 1, 
            number: 99
        }).then((res) => {
            if (res.code == 0) {
                res.data.map((item, index) => {
                    item.key = item.id;
                    item.createTime = moment.unix(item.createTime / 1000).format('YYYY-MM-DD hh:mm');
                })
                this.setState({
                    listData: res.data
                })
            } else {
                
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
        console.log(fields)
        addChannel({
            ...fields
        }).then(res => {
            if (res.code == 0) {
                this.setState({
                    modalVisible: false,
                });
                message.success('添加成功');
                this.getChannelList()
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error('添加失败，请重试！');
            console.log(err);
        })
    }

    deleteChannel = (ids) => {
        const id = ids.join(',');
        console.log(id);
        removeChannels({
            id: id
        }).then(res => {
            if (res.code == 0) {
                message.success('删除成功');
                this.getChannelList();
            } else {
                message.error(res.msg);
            }
        }).catch(err => {
            message.error('删除失败');
            console.log(err);
        })
    }

    handleInputChange = (e, key) => {
        console.log(e.target.value);
        if (key == 'name') {
            this.setState({
                name: e.target.value
            })
        } else if (key == 'invcode') {
            this.setState({
                invcode: e.target.value
            })
        }
        // e.detail.value
    }

    renderSimpleForm() {
        return (
            <Form layout="inline">
                <Row gutter={{ md: 8, lg: 24, xl: 48 }}>

                    <Col md={6} sm={24}>
                        <FormItem label="业务渠道名称">
                            <Input placeholder='请输入渠道名称' value={this.state.name} onChange={(e) => {this.handleInputChange(e, "name")}} />
                        </FormItem>
                    </Col>
                    <Col md={6} sm={24}>
                        <FormItem label="邀请码">
                            <Input placeholder='请输入邀请码' value={this.state.invcode} onChange={(e) => {this.handleInputChange(e, "invcode")}} />
                        </FormItem>
                    </Col>

                    <Col md={8} sm={24}>
                        <span className={styles.submitButtons}>
                            <Button type="primary" htmlType="submit" onClick={this.getChannelList} >查询</Button>
                            <Button style={{ marginLeft: 30 }} onClick={this.handleFormReset}>重置</Button>
                        </span>
                    </Col>
                </Row>
            </Form>
        );
    }

    render() {

        const parentMethods = {
            handleAdd: this.handleAdd,
            handleModalVisible: this.handleModalVisible,
        };

        return (
            <PageHeaderLayout title="渠道管理">
                <Card bordered={false}>
                    <div className={styles.tableList}>
                        <div className={styles.tableListForm}>
                            {this.renderSimpleForm()}
                        </div>
                        <div className={styles.tableListOperator} style={{ marginTop: 50 }}>
                            <Button onClick={() => {this.setState({modalVisible: true})}} >新增</Button>
                            <Button style={{ marginLeft: 20 }}>批量删除</Button>
                        </div>
                        <Table
                            //   loading={loading}
                            // data={data}
                            dataSource={this.state.listData}
                            columns={this.state.columns}
                        />
                    </div>
                </Card>
                <CreateForm
                    {...parentMethods}
                    modalVisible={this.state.modalVisible}
                />
            </PageHeaderLayout >
        );
    }
}
