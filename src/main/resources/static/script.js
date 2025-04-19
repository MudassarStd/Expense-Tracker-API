let editingId = null;

function toggleFilters() {
  document.getElementById('filterForm').classList.toggle('active');
}

async function fetchExpenses(params = '') {
  const res = await fetch('/expense' + params);
  const data = await res.json();

  const container = document.getElementById('expenses');
  container.innerHTML = '';
  data.forEach(exp => {
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
    container.appendChild(row);
  });
}

async function fetchTotalAmount() {
  const res = await fetch('/expense/total');
  const totalAmount = await res.json();
  document.getElementById('totalAmountCard').querySelector('p').textContent = `Rs.${totalAmount}`;

  const resIncome = await fetch('/expense/total/type?type=INCOME');
  const totalIncome = await resIncome.json();
  document.getElementById('totalIncomeCard').querySelector('p').textContent = `Rs.${totalIncome}`;

  const resExpense = await fetch('/expense/total/type?type=EXPENSE');
  const totalExpense = await resExpense.json();
  document.getElementById('totalExpenseCard').querySelector('p').textContent = `Rs.${totalExpense}`;
}

document.getElementById('expenseForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const data = Object.fromEntries(formData);

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
  fetchExpenses();
  fetchTotalAmount();
});

document.getElementById('filterForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const params = new URLSearchParams();
  for (const [key, value] of formData.entries()) {
    if (value) params.append(key, value);
  }
  fetchExpenses('?' + params.toString());
});

async function deleteExpense(id) {
  if (!confirm("Are you sure you want to delete this expense?")) return;

  await fetch(`/expense/delete/${id}`, {
    method: 'DELETE'
  });

  fetchExpenses();
  fetchTotalAmount();
}

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

// Initial load
fetchExpenses();
fetchTotalAmount();
