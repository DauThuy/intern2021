<template>
    <div class="task">
        <span>{{taskData.content}}</span>
    </div>
</template>

<script>
export default {
    // props: ['taskData'], //Báo cho component biết là sẽ lấy props là tashData từ bên ngoài vào
   name: "Task",
   props: {
       taskData: Object,
       default: function(){return 0;}
   },
   data() {
        return {
            
        }
    },
    methods: {
        deleteTask: function() {
            console.log(this.taskData);
        }
    }
}

</script>

<style scoped>

</style>
