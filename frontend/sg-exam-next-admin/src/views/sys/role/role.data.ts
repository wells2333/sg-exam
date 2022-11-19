import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { updateRoleStatus } from '/@/api/sys/role';
import { useMessage } from '/@/hooks/web/useMessage';

export const columns: BasicColumn[] = [
  {
    title: '角色名称',
    dataIndex: 'roleName',
    width: 200,
  },
  {
    title: '角色代码',
    dataIndex: 'roleCode',
    width: 180,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: record.status === 0,
        checkedChildren: '已启用',
        unCheckedChildren: '已禁用',
        loading: record.pendingStatus,
        onChange(checked: boolean) {
          record.pendingStatus = true;
          const newStatus = checked ? 0 : 1;
          const { createMessage } = useMessage();
          updateRoleStatus(record.id, { status: newStatus })
            .then(() => {
              record.status = newStatus;
              createMessage.success(`已成功修改角色状态`);
            })
            .catch(() => {
              createMessage.error('修改角色状态失败');
            })
            .finally(() => {
              record.pendingStatus = false;
            });
        },
      });
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
  {
    title: '操作人',
    dataIndex: 'operator',
    width: 100,
  },
  {
    title: '备注',
    dataIndex: 'roleDesc',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'roleNme',
    label: '角色名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Render',
    show: false,
  },
  {
    field: 'roleName',
    label: '角色名称',
    required: true,
    component: 'Input',
  },
  {
    field: 'roleCode',
    label: '角色代码',
    required: true,
    component: 'Input',
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '启用', value: 0 },
        { label: '停用', value: 1 },
      ],
    },
    required: true,
  },
  {
    label: '备注',
    field: 'roleDesc',
    component: 'InputTextArea',
  },
  {
    label: ' ',
    field: 'menuIds',
    slot: 'menu',
    component: 'Input',
  },
];
