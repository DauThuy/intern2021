<template>
  <div id="app">
    <div>
      <h1 v-show="isShow">{{name}}</h1>
    <button @click="isShow=!isShow">
      <span v-if="isShow">Hide</span>
      <span v-else>Show</span>
    </button>
    </div>
    <hr>
    <br>
    <div>
      <input type="text" v-model="newTask"> <!--khi nhap text vao input, se tu dong map baang bien newTask-->
      <button @click="addTask">Add</button>
     
      <div v-for="(task, index) in tasks" :key="index"> <!--Mỗi task là 1 obj có 2 thuộc tính content, done-->
        <input type="checkbox" v-model="task.done"> <!--bind data the input voi task.done - status hien tai thi checkbox se khong tich -->
        <span :class="{done1: task.done}"> <!-- span này sẽ ăn class done nếu task.done=true -->
          {{index}} {{task.content}}
        </span>
      </div>
    </div>
    <hr>
    <Task v-for="(task, index) in tasks" :key="index" :taskData="task" />
    <hr>
    <TagSelector />

    <hr>
    <div>Tong tien:
      {{tongTien | dauChamPhanTach}}
      <button @click="tongTien += 2000">Calculatte</button>
    </div>
  </div>

</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import Task from './components/Task.vue'
import TagSelector from './components/TagSelector.vue'

export default {
  name: 'App',
  data() {
    return {
      name: 'Dau Thuy',
      isShow: true,
      tasks: [ //load API chỉ cần đổ dữ liệu vào đây
        {content: 'di cho', done: false},
        {content: 'nau an', done: false},
        {content: 'code', done: false},
        {content: 'rua chen', done: false},
      ],
      newTask: '',
      tongTien: 1000000,
    }
  },
  filters: {
    dauChamPhanTach: function(soTien) {
      return soTien.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }
  },

  components: {
    Task,
    TagSelector,
  },

  mounted() {
    //chạy đầu tiên, load API
  },

  methods: {
      addTask: function() {
        this.tasks.push({
          content: this.newTask, 
          done: false
        });
      }
  },

  watch: { //theo dõi biến newTask - phân page
    newTask: function(newValue, oldValue) {
      console.log('oldValue: ' + oldValue);
      console.log('newValue: ' + newValue);
    }
  }
}
</script>

<style>
.done1 {
  text-decoration: line-through;
}
</style>
