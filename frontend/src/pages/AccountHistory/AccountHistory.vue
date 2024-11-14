<template>
  <div>
    <Navbar />
    <div class="container">
      <div class="header">
        <button @click="prevMonth">＜</button>
        <span>{{ currentMonth }}</span>
        <button @click="nextMonth">＞</button>
      </div>

      <div class="chart-section">
        <canvas id="pieChart"></canvas>
      </div>

      <div class="balance-section">
        <div class="balance-bar">
          <div class="deposit" :style="{ width: depositPercentage + '%' }"></div>
          <div class="withdrawal" :style="{ width: withdrawalPercentage + '%' }"></div>
        </div>
        <div class="balance-info">
          <span>입금 {{ depositPercentage }}%</span>
          <span>출금 {{ withdrawalPercentage }}%</span>
        </div>
      </div>

      <div class="transaction-list">
        <div class="transaction-item" v-for="(transaction, index) in transactions" :key="index">
          <div class="date">{{ transaction.date }}</div>
          <div class="details">
            <img :src="transaction.icon" alt="Icon" class="transaction-icon">
            <span>{{ transaction.description }}</span>
            <span :class="transaction.type">{{ transaction.amount }}원</span>
          </div>
          <div class="balance">잔액 {{ transaction.balance }}원</div>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { Chart, PieController, ArcElement, Tooltip, Legend } from 'chart.js';
import Navbar from '@/components/Navbar.vue';

// 필요한 컴포넌트 등록
Chart.register(PieController, ArcElement, Tooltip, Legend);

const currentMonth = ref("2024.10");

const transactions = ref([
  { date: "2024. 10. 30", icon: "../../assets/계절밥상.png", description: "계절밥상", type: "withdraw", amount: "7,000", balance: "7,000,000" },
  { date: "2024. 10. 30", icon: "../../assets/홈플러스.png", description: "홈플러스", type: "deposit", amount: "7,000", balance: "7,000,000" },
  // 추가적인 거래 항목을 여기에 입력
]);

const depositPercentage = 51;
const withdrawalPercentage = 49;

function prevMonth() {
  const [year, month] = currentMonth.value.split(".");
  const newDate = new Date(year, month - 2, 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(newDate.getMonth() + 1).padStart(2, "0")}`;
}

function nextMonth() {
  const [year, month] = currentMonth.value.split(".");
  const newDate = new Date(year, month, 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(newDate.getMonth() + 1).padStart(2, "0")}`;
}

onMounted(() => {
  const ctx = document.getElementById("pieChart").getContext("2d");
  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: ['입금', '출금'],
      datasets: [{
        data: [depositPercentage, withdrawalPercentage],
        backgroundColor: ['#4CAF50', '#FF5733'],
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top',
        },
      },
    },
  });
});
</script>

<style scoped>
.container {
  background-color: #FFF5F2;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 0;
}

.header button {
  background: none;
  border: none;
  font-size: 18px;
  color: #333;
  margin: 0 10px;
}

.chart-section {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.pie-chart {
  width: 150px;
  height: 150px;
}

.balance-section {
  background-color: #FFFFFF;
  border-radius: 10px;
  padding: 10px;
  margin: 10px 0;
}

.balance-bar {
  display: flex;
  height: 10px;
  border-radius: 5px;
  overflow: hidden;
}

.deposit {
  background-color: #4CAF50;
}

.withdrawal {
  background-color: #FF5733;
}

.balance-info {
  display: flex;
  justify-content: space-between;
  padding-top: 5px;
  font-size: 12px;
  color: #666;
}

.transaction-list {
  background-color: #FFFFFF;
  border-radius: 10px;
  padding: 10px;
}

.transaction-item {
  border-bottom: 1px solid #E5E5E5;
  padding: 10px 0;
}

.transaction-item:last-child {
  border-bottom: none;
}

.date {
  font-size: 12px;
  color: #999;
}

.details {
  display: flex;
  align-items: center;
}

.transaction-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.deposit, .withdrawal {
  font-size: 14px;
}

.withdraw {
  color: #FF5733;
}

.deposit {
  color: #4CAF50;
}

.balance {
  font-size: 12px;
  color: #666;
}
</style>
