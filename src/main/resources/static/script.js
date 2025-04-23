<script>
let currentPage = 0;
const pageSize = 5;

function toggleFilters() {
  document.getElementById('filterForm').classList.toggle('active');
}

async function fetchExpenses(params = '') {
  const fullParams = new URLSearchParams(params);
  fullParams.set('page', currentPage);
  fullParams.set('pageSize', pageSize);

  const res = await fetch('/expense?' + fullParams.toString());
  const data = await res.json();

  const expensesTable = document.getElementById('expenses');
  expensesTable.innerHTML = '';

  data.expenses.forEach(exp => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td class="expense-amount ${exp.type === 'EXPENSE' ? 'type-expense' : 'type-income'}">Rs.${exp.amount}</td>
      <td>${exp.description}</td>
      <td><span class="category-tag">${exp.category}</span></td>
      <td>${exp.type}</td>
      <td>${exp.date}</td>
      <td class="action-icons">
        <i class="fas fa-edit" title="Edit" onclick="editExpense(${exp.id})"></i>
        <i class="fas fa-trash" title="Delete" onclick="deleteExpense(${exp.id})"></i>
      </td>
    `;
    expensesTable.appendChild(row);
  });

  // Fix pagination button logic
  document.getElementById('prevPage').disabled = currentPage === 0;
  document.getElementById('nextPage').disabled = !data.hasNextPage;

  // Optional: update page indicator
  document.getElementById('pageInfo').textContent = `Page ${currentPage + 1}`;
}


async function fetchTotalAmount() {
  const res = await fetch('/expense/total');
  const total = await res.json();
  document.getElementById('totalAmountCard').querySelector('p').textContent = `Rs.${total}`;

  const incomeRes = await fetch('/expense/total/type?type=INCOME');
  const income = await incomeRes.json();
  document.getElementById('totalIncomeCard').querySelector('p').textContent = `Rs.${income}`;

  const expenseRes = await fetch('/expense/total/type?type=EXPENSE');
  const expense = await expenseRes.json();
  document.getElementById('totalExpenseCard').querySelector('p').textContent = `Rs.${expense}`;
}

document.getElementById('expenseForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const data = Object.fromEntries(formData.entries());

  data.amount = parseFloat(data.amount);
  data.date = new Date(data.date).toISOString().split('T')[0];

  const url = editingId ? `/expense/update/${editingId}` : '/expense';
  const method = editingId ? 'PUT' : 'POST';

  await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  editingId = null;
  e.target.reset();
  fetchExpenses(lastFilterParams);
  fetchTotalAmount();
});

let lastFilterParams = new URLSearchParams();

document.getElementById('filterForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const params = new URLSearchParams();
  for (const [key, value] of formData.entries()) {
    if (value) params.append(key, value);
  }
  lastFilterParams = params;
  currentPage = 0;
  fetchExpenses(params);
});

document.getElementById('prevPage').addEventListener('click', () => {
  if (currentPage > 0) {
    currentPage--;
    fetchExpenses(lastFilterParams);
  }
});

document.getElementById('nextPage').addEventListener('click', () => {
  currentPage++;
  fetchExpenses(lastFilterParams);
});

function editExpense(id) {
  fetch(`/expense/find/${id}`)
    .then(res => res.json())
    .then(exp => {
      const form = document.getElementById('expenseForm');
      form.amount.value = exp.amount;
      form.description.value = exp.description;
      form.category.value = exp.category;
      form.type.value = exp.type;
      form.date.value = exp.date;
      editingId = exp.id;
    });
}

async function deleteExpense(id) {
  if (!confirm("Are you sure you want to delete this expense?")) return;
  await fetch(`/expense/delete/${id}`, { method: 'DELETE' });
  fetchExpenses(lastFilterParams);
  fetchTotalAmount();
}

// Initial load
fetchExpenses();
fetchTotalAmount();
</script>
