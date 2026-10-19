import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const authApi = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (fullName, email, password, phone) => 
    api.post('/auth/register', { fullName, email, password, phone }),
  getProfile: (userId) => api.get(`/auth/me/${userId}`),
  updateProfile: (userId, data) => api.put(`/auth/profile/${userId}`, data),
};

export const jobApi = {
  getJobs: (params = {}) => api.get('/jobs', { params }),
  getJobById: (id, userId) => api.get(`/jobs/${id}`, { params: { userId } }),
  createJob: (jobData, skillIds) => api.post('/jobs', jobData, { params: { skillIds } }),
};

export const skillApi = {
  getAllSkills: () => api.get('/skills'),
  getUserSkills: (userId) => api.get(`/skills/user/${userId}`),
  addSkillToUser: (userId, skillData) => api.post(`/skills/user/${userId}/add`, skillData),
  removeSkillFromUser: (userId, skillId) => api.delete(`/skills/user/${userId}/remove/${skillId}`),
  createSkill: (skillName, category) => api.post('/skills/create', { skillName, category }),
};

export const applicationApi = {
  apply: (userId, jobId) => api.post('/applications/apply', { userId, jobId }),
  getUserApplications: (userId) => api.get(`/applications/user/${userId}`),
};

export const resumeApi = {
  uploadResume: (userId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post(`/resume/upload/${userId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
  getDownloadUrl: (userId) => `${API_BASE_URL}/resume/download/${userId}`,
};

export default api;
