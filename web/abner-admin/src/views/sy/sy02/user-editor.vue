<template>
  <Graces.FormEditor
      module="sy02"
      name="userEdit"
      @on-save="onSave"
      :fill="!!id"
      :params="{id: id}"
      :form-rules="formRules"
      :form-value="formValue"
  />
</template>

<script setup lang="ts">
  import {useRoute} from "vue-router";
  import {onBeforeMount, type Ref, shallowRef} from 'vue';
  /********************************************************************************
   * 新增用户
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces, http, cryptoHelper, message, loading} from '@/ploutos';

  /**
   * 路由
   */
  const route = useRoute();

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    value: {
      type: String
    }
  });

  /**
   * id
   */
  const id = route.params.id || props.value;

  /**
   * 自定义字段校验规则
   */
  let formValue: Ref = shallowRef({});
  let formRules: Ref = shallowRef({});

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    formRules.value = {
      password: [{
        required: true,
        message: '请输入密码',
        trigger: ['blur', 'input'],
      }, {
        validator: validatePasswordLength,
        message: '密码长度必须大于6位',
        trigger: ['blur', 'input']
      }],
      confirmPwd: [{
        required: true,
        message: '请确认密码',
        trigger: ['blur', 'input'],
      }, {
        validator: validatePasswordSame,
        message: '两次密码输入不一致',
        trigger: ['blur', 'input']
      }]
    }
  });

  /**
   * 校验密码长度
   */
  function validatePasswordLength(rule: any, value: string): boolean {
    return value && value.length > 6;
  }

  /**
   * 校验两次密码输入是否一致
   */
  function validatePasswordSame(rule: any, value: string): boolean {
    return value && value == formValue.value.password;
  }

  /**
   * 提交
   */
  async function onSave(value: any) {

    try {
      loading(true);
      const data: any = {...value};
      let key = "";

      // 密码加密
      if (!value.id) {
        key = cryptoHelper.generate256BitKey();
        data.password = cryptoHelper.aesEncrypt(key, data.password);
      }
      await http.post("/sy02/save?key=" + key, data);
      message.success('用户[ ' + data.username + ' ]' + (key ? '新增' : '修改' + '成功'));

      // 刷新列表
      const func = new Function( 'name', 'return eval(name).call()');
      func('sy02UserListRefresh');
    } finally {
      loading(false);
    }
  }
</script>

<style scoped lang="scss">

</style>