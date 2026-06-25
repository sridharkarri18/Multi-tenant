const API_BASE = '/api';
let authToken = null;
let currentRole = null;

// DOM Elements
const loginScreen = document.getElementById('login-screen');
const dashboardScreen = document.getElementById('dashboard-screen');
const loginForm = document.getElementById('login-form');
const loginError = document.getElementById('login-error');
const logoutBtn = document.getElementById('logout-btn');
const userInfo = document.getElementById('user-info');
const featureKeyInput = document.getElementById('feature-key-input');
const checkFlagBtn = document.getElementById('check-flag-btn');
const flagResult = document.getElementById('flag-result');
const dynamicFeatureArea = document.getElementById('dynamic-feature-area');

const orgAdminArea = document.getElementById('org-admin-area');
const createKeyInput = document.getElementById('create-key-input');
const createFlagBtn = document.getElementById('create-flag-btn');
const deleteKeyInput = document.getElementById('delete-key-input');
const deleteFlagBtn = document.getElementById('delete-flag-btn');
const adminResult = document.getElementById('admin-result');

// --- LOGIN LOGIC ---
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginError.textContent = '';
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const submitBtn = loginForm.querySelector('button');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Authenticating...';
    
    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        if (!response.ok) {
            throw new Error('Invalid credentials');
        }
        
        const data = await response.json();
        authToken = data.token;
        currentRole = data.role;
        
        // Setup Dashboard
        userInfo.textContent = `Logged in as: ${email} (${data.role})`;
        
        const featureCheckerContainer = document.querySelector('.feature-checker');
        
        // Show proper sections based on role
        if (data.role === 'SUPER_ADMIN') {
            superAdminArea.classList.remove('hidden');
            featureCheckerContainer.classList.add('hidden'); // Hide flag check for Super Admin
            loadOrganizations(); // Fetch orgs for the dropdown immediately
        } else if (data.role === 'ORG_ADMIN') {
            orgAdminArea.classList.remove('hidden');
            featureCheckerContainer.classList.remove('hidden');
        } else {
            // END_USER sees neither
            superAdminArea.classList.add('hidden');
            orgAdminArea.classList.add('hidden');
            featureCheckerContainer.classList.remove('hidden');
            
            // Automatically check dark-mode flag upon login!
            fetch(`${API_BASE}/user/flags/check?featureKey=dark-mode`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            })
            .then(res => {
                if (res.ok) return res.json();
                throw new Error('Flag not found');
            })
            .then(flagData => {
                // User said it was working opposite, so let's flip it
                // If true -> light theme, If false -> dark theme
                if (flagData.enabled) {
                    document.body.classList.add('light-theme');
                } else {
                    document.body.classList.remove('light-theme');
                }
            })
            .catch(() => {
                // If flag doesn't exist, default to dark
                document.body.classList.remove('light-theme');
            });
        }
        
        // Transition screens
        loginScreen.classList.remove('active');
        loginScreen.classList.add('hidden');
        dashboardScreen.classList.remove('hidden');
        dashboardScreen.classList.add('active');
        
    } catch (err) {
        loginError.textContent = 'Login failed. Please check your credentials.';
        loginError.style.animation = 'none';
        setTimeout(() => loginError.style.animation = 'fadeIn 0.3s ease', 10);
    } finally {
        submitBtn.textContent = originalText;
    }
});

// --- LOGOUT LOGIC ---
logoutBtn.addEventListener('click', () => {
    authToken = null;
    currentRole = null;
    
    // Reset UI
    featureKeyInput.value = '';
    createKeyInput.value = '';
    deleteKeyInput.value = '';
    flagResult.classList.add('hidden');
    adminResult.classList.add('hidden');
    dynamicFeatureArea.classList.add('hidden');
    orgAdminArea.classList.add('hidden');
    superAdminArea.classList.add('hidden'); // Also hide Super Admin UI
    document.body.classList.remove('light-theme');
    
    // Transition screens
    dashboardScreen.classList.remove('active');
    dashboardScreen.classList.add('hidden');
    loginScreen.classList.remove('hidden');
    loginScreen.classList.add('active');
});

// --- CREATE FEATURE FLAG LOGIC (ORG ADMIN) ---
createFlagBtn.addEventListener('click', async () => {
    const key = createKeyInput.value.trim();
    if (!key) return;
    
    createFlagBtn.textContent = '...';
    adminResult.classList.add('hidden');
    
    try {
        const response = await fetch(`${API_BASE}/orgadmin/flags`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({
                featureKey: key,
                enabled: true
            })
        });
        
        if (!response.ok) {
            const errData = await response.json().catch(() => ({}));
            throw new Error(errData.error || 'Failed to create flag.');
        }
        
        const data = await response.json();
        adminResult.textContent = `✅ Successfully created flag: ${data.featureKey}`;
        adminResult.classList.remove('hidden', 'false');
        adminResult.classList.add('true');
        createKeyInput.value = '';
        
    } catch (err) {
        adminResult.textContent = `❌ ${err.message}`;
        adminResult.classList.remove('hidden', 'true');
        adminResult.classList.add('false');
    } finally {
        createFlagBtn.textContent = 'Create';
    }
});

// --- DELETE FEATURE FLAG LOGIC (ORG ADMIN) ---
deleteFlagBtn.addEventListener('click', async () => {
    const key = deleteKeyInput.value.trim();
    if (!key) return;
    
    deleteFlagBtn.textContent = '...';
    adminResult.classList.add('hidden');
    
    try {
        const response = await fetch(`${API_BASE}/orgadmin/flags/${key}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });
        
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Not Found: That flag does not exist.');
            }
            const errData = await response.json().catch(() => ({}));
            throw new Error(errData.error || 'Failed to delete flag.');
        }
        
        adminResult.textContent = `🗑️ Successfully deleted flag: ${key}`;
        adminResult.classList.remove('hidden', 'false');
        adminResult.classList.add('true');
        deleteKeyInput.value = '';
        
    } catch (err) {
        adminResult.textContent = `❌ ${err.message}`;
        adminResult.classList.remove('hidden', 'true');
        adminResult.classList.add('false');
    } finally {
        deleteFlagBtn.textContent = 'Delete';
    }
});

// --- CHECK FEATURE FLAG LOGIC ---
checkFlagBtn.addEventListener('click', async () => {
    const key = featureKeyInput.value.trim();
    if (!key) return;
    
    checkFlagBtn.textContent = '...';
    
    try {
        const response = await fetch(`${API_BASE}/user/flags/check?featureKey=${key}`, {
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });
        
        if (!response.ok) {
            if(response.status === 403) throw new Error('Forbidden: Only END_USER role can check flags.');
            if(response.status === 400) throw new Error('Feature flag not found for your organization.');
            throw new Error('Server error');
        }
        
        const data = await response.json();
        
        // Display result JSON
        flagResult.textContent = JSON.stringify(data, null, 2);
        flagResult.classList.remove('hidden', 'true', 'false');
        flagResult.classList.add(data.enabled ? 'true' : 'false');
        
        // Dynamic UI Change based on Flag Status!
        if (data.enabled) {
            dynamicFeatureArea.classList.remove('hidden');
        } else {
            dynamicFeatureArea.classList.add('hidden');
        }
        
        // If the flag is about dark mode, physically toggle the site theme!
        if (key.toLowerCase().includes('dark')) {
            if (data.enabled) {
                document.body.classList.add('light-theme'); // True -> Light Mode (per user request)
            } else {
                document.body.classList.remove('light-theme'); // False -> Dark Mode
            }
        }
        
    } catch (err) {
        flagResult.textContent = err.message;
        flagResult.classList.remove('hidden', 'true');
        flagResult.classList.add('false');
        dynamicFeatureArea.classList.add('hidden');
        document.body.classList.remove('light-theme');
    } finally {
        checkFlagBtn.textContent = 'Evaluate';
    }
});

// --- ORG ADMIN LOGIC & DOM ---
const tabFlags = document.getElementById('tab-flags');
const tabOrgUsers = document.getElementById('tab-org-users');
const viewFlags = document.getElementById('view-flags');
const viewOrgUsers = document.getElementById('view-org-users');
const orgAdminUserEmail = document.getElementById('org-admin-user-email');
const orgAdminUserPassword = document.getElementById('org-admin-user-password');
const orgAdminCreateUserBtn = document.getElementById('org-admin-create-user-btn');
const orgAdminUserResult = document.getElementById('org-admin-user-result');

// Org Admin Tab Switching
tabFlags.addEventListener('click', () => {
    tabFlags.classList.add('active-tab');
    tabOrgUsers.classList.remove('active-tab');
    viewFlags.classList.remove('hidden');
    viewOrgUsers.classList.add('hidden');
});

tabOrgUsers.addEventListener('click', () => {
    tabOrgUsers.classList.add('active-tab');
    tabFlags.classList.remove('active-tab');
    viewOrgUsers.classList.remove('hidden');
    viewFlags.classList.add('hidden');
});

// Org Admin Create User
orgAdminCreateUserBtn.addEventListener('click', async () => {
    const email = orgAdminUserEmail.value.trim();
    const password = orgAdminUserPassword.value.trim();
    if (!email || !password) return;
    
    orgAdminCreateUserBtn.textContent = '...';
    try {
        const res = await fetch(`${API_BASE}/orgadmin/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${authToken}` },
            body: JSON.stringify({ email, password })
        });
        if(!res.ok) {
            const errData = await res.json().catch(() => ({}));
            throw new Error(errData.error || 'Failed to create end user');
        }
        
        orgAdminUserResult.textContent = `✅ Successfully created user: ${email}`;
        orgAdminUserResult.className = 'result-box true';
        orgAdminUserEmail.value = '';
        orgAdminUserPassword.value = '';
    } catch(err) {
        orgAdminUserResult.textContent = `❌ ${err.message}`;
        orgAdminUserResult.className = 'result-box false';
    } finally {
        orgAdminCreateUserBtn.textContent = 'Create End User';
    }
});

// --- SUPER ADMIN LOGIC & DOM ---
const superAdminArea = document.getElementById('super-admin-area');
const tabOrg = document.getElementById('tab-org');
const tabUser = document.getElementById('tab-user');
const viewOrg = document.getElementById('view-org');
const viewUser = document.getElementById('view-user');

const createOrgInput = document.getElementById('create-org-input');
const createOrgBtn = document.getElementById('create-org-btn');
const orgResult = document.getElementById('org-result');

const userOrgSelect = document.getElementById('user-org-select');
const userRoleSelect = document.getElementById('user-role-select');
const userEmailInput = document.getElementById('user-email-input');
const userPasswordInput = document.getElementById('user-password-input');
const createUserBtn = document.getElementById('create-user-btn');
const userResult = document.getElementById('user-result');

const orgUserListContainer = document.getElementById('org-user-list-container');
const orgUserCount = document.getElementById('org-user-count');
const orgUserList = document.getElementById('org-user-list');

// Super Admin Tab Switching
tabOrg.addEventListener('click', () => {
    tabOrg.classList.add('active-tab');
    tabUser.classList.remove('active-tab');
    viewOrg.classList.remove('hidden');
    viewUser.classList.add('hidden');
});

tabUser.addEventListener('click', () => {
    tabUser.classList.add('active-tab');
    tabOrg.classList.remove('active-tab');
    viewUser.classList.remove('hidden');
    viewOrg.classList.add('hidden');
    // Fetch users for currently selected org if any
    if (userOrgSelect.value) {
        loadOrgUsers(userOrgSelect.value);
    }
});

// Listen for organization dropdown changes
userOrgSelect.addEventListener('change', (e) => {
    if (e.target.value) {
        loadOrgUsers(e.target.value);
    } else {
        orgUserListContainer.classList.add('hidden');
    }
});

async function loadOrgUsers(orgId) {
    try {
        const res = await fetch(`${API_BASE}/superadmin/organizations/${orgId}/users`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        if (!res.ok) throw new Error();
        const users = await res.json();
        
        orgUserCount.textContent = users.length;
        orgUserList.innerHTML = '';
        
        users.forEach(u => {
            const div = document.createElement('div');
            div.className = 'user-item';
            div.innerHTML = `<span>${u.email}</span> <span class="user-role-badge">${u.role}</span>`;
            orgUserList.appendChild(div);
        });
        
        orgUserListContainer.classList.remove('hidden');
    } catch(err) {
        orgUserListContainer.classList.add('hidden');
    }
}

// Fetch Organizations
async function loadOrganizations() {
    try {
        const res = await fetch(`${API_BASE}/superadmin/organizations`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        if (!res.ok) throw new Error();
        const orgs = await res.json();
        
        userOrgSelect.innerHTML = '';
        if (orgs.length === 0) {
            userOrgSelect.innerHTML = '<option value="">No organizations exist</option>';
            orgUserListContainer.classList.add('hidden');
        } else {
            orgs.forEach(org => {
                const opt = document.createElement('option');
                opt.value = org.id;
                opt.textContent = `${org.id} - ${org.name}`;
                userOrgSelect.appendChild(opt);
            });
            // Automatically load users for the first organization
            loadOrgUsers(orgs[0].id);
        }
    } catch(err) {
        userOrgSelect.innerHTML = '<option value="">Error loading orgs</option>';
    }
}

// Create Organization
createOrgBtn.addEventListener('click', async () => {
    const name = createOrgInput.value.trim();
    if (!name) return;
    
    createOrgBtn.textContent = '...';
    try {
        const res = await fetch(`${API_BASE}/superadmin/organizations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${authToken}` },
            body: JSON.stringify({ name })
        });
        if(!res.ok) throw new Error('Failed to create organization');
        const data = await res.json();
        
        orgResult.textContent = `✅ Created Organization ID: ${data.id}`;
        orgResult.className = 'result-box true';
        createOrgInput.value = '';
        loadOrganizations(); // Reload dropdown
    } catch (err) {
        orgResult.textContent = `❌ ${err.message}`;
        orgResult.className = 'result-box false';
    } finally {
        createOrgBtn.textContent = 'Create Org';
    }
});

// Provision User
createUserBtn.addEventListener('click', async () => {
    const email = userEmailInput.value.trim();
    const password = userPasswordInput.value.trim();
    const role = userRoleSelect.value;
    const orgId = userOrgSelect.value;
    
    if (!email || !password || !orgId) return;
    
    createUserBtn.textContent = '...';
    const endpoint = role === 'ORG_ADMIN' ? '/auth/signup/org-admin' : '/auth/signup/user';
    
    try {
        const res = await fetch(`${API_BASE}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, organizationId: parseInt(orgId) })
        });
        
        if(!res.ok) {
            const errData = await res.json().catch(() => ({}));
            throw new Error(errData.error || 'Failed to register user');
        }
        
        userResult.textContent = `✅ Successfully created ${role} for Org ID: ${orgId}`;
        userResult.className = 'result-box true';
        userEmailInput.value = '';
        userPasswordInput.value = '';
        
        // Refresh the list!
        loadOrgUsers(orgId);
    } catch(err) {
        userResult.textContent = `❌ ${err.message}`;
        userResult.className = 'result-box false';
    } finally {
        createUserBtn.textContent = 'Create User';
    }
});
