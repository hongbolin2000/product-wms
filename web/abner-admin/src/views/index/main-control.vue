<template>
  <div>
    <n-button @click="show = true"></n-button>

    <n-modal v-model:show="show">
      <div>
        <n-card
            title="新增门店"
            :segmented="{
            content: true,
      }"
        >
          <n-form>
            <n-grid :cols="24" :x-gap="24">
              <n-form-item-gi :span="6" label="姓名" path="user.name">
                <n-input placeholder="输入姓名" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="年龄" path="user.age">
                <n-input placeholder="输入年龄" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
              <n-form-item-gi :span="6" label="电话号码" path="phone">
                <n-input placeholder="电话号码" />
              </n-form-item-gi>
            </n-grid>
          </n-form>
        </n-card>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts" name="MainControl">/********************************************************************************
 * @author Berlin
 ********************************************************************************/
  import {ref} from "vue";

  const formValue = ref({});

  const show = ref(false);
</script>
