import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { authApi, resumeApi } from '../services/api';
import {
  User,
  Mail,
  Phone,
  Calendar,
  FileText,
  Upload,
  Download,
  CheckCircle,
  AlertCircle,
  Save,
  Award,
} from 'lucide-react';

const Profile = () => {
  const { user, refreshUser } = useAuth();
  const [fullName, setFullName] = useState(user?.fullName || '');
  const [phone, setPhone] = useState(user?.phone || '');
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    if (!user) return;
    setSaving(true);
    try {
      const res = await authApi.updateProfile(user.id, { fullName, phone });
      if (res.data?.success) {
        showToast('Profile information updated successfully!');
        refreshUser(user.id);
      }
    } catch (err) {
      showToast(err.response?.data?.message || 'Failed to update profile', 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleFileUpload = async (e) => {
    const file = e.target.files?.[0];
    if (!file || !user) return;

    if (!file.name.toLowerCase().endsWith('.pdf') && !file.name.toLowerCase().endsWith('.docx')) {
      showToast('Please upload a PDF or Word (.docx) document', 'error');
      return;
    }

    setUploading(true);
    try {
      const res = await resumeApi.uploadResume(user.id, file);
      if (res.data?.success) {
        showToast('Resume uploaded and attached to your profile!');
        refreshUser(user.id);
      }
    } catch (err) {
      showToast(err.response?.data?.message || 'Failed to upload resume', 'error');
    } finally {
      setUploading(false);
      e.target.value = '';
    }
  };

  const downloadUrl = user ? resumeApi.getDownloadUrl(user.id) : '#';

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>User Profile & Resume</h2>
          <p>Manage your account credentials, contact info, and uploaded resume</p>
        </div>
      </div>

      {toast && (
        <div className={`notification-toast ${toast.type === 'success' ? 'toast-success' : 'toast-error'}`}>
          {toast.type === 'success' ? <CheckCircle size={18} /> : <AlertCircle size={18} />}
          <span>{toast.message}</span>
        </div>
      )}

      <div className="profile-grid">
        {/* Left: Profile Information */}
        <div className="card">
          <div className="card-header">
            <h3>Personal Information</h3>
          </div>

          <form onSubmit={handleUpdateProfile} className="profile-form">
            <div className="form-group">
              <label>Full Name</label>
              <div className="input-with-icon">
                <User size={18} className="input-icon" />
                <input
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label>Email Address</label>
              <div className="input-with-icon">
                <Mail size={18} className="input-icon" />
                <input
                  type="email"
                  value={user?.email || ''}
                  disabled
                  className="input-disabled"
                />
              </div>
              <span className="text-xs text-muted">Email cannot be changed</span>
            </div>

            <div className="form-group">
              <label>Phone Number</label>
              <div className="input-with-icon">
                <Phone size={18} className="input-icon" />
                <input
                  type="tel"
                  placeholder="+91 9876543210"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                />
              </div>
            </div>

            <button type="submit" disabled={saving} className="btn-primary mt-2">
              <Save size={18} />
              <span>{saving ? 'Saving changes...' : 'Save Changes'}</span>
            </button>
          </form>
        </div>

        {/* Right: Resume Management */}
        <div className="card">
          <div className="card-header">
            <h3>Resume Document</h3>
            <p className="card-subtitle">Upload your latest resume (PDF recommended)</p>
          </div>

          <div className="resume-status-box">
            {user?.resumePath ? (
              <div className="resume-attached">
                <div className="file-badge-large">
                  <FileText size={36} color="#2563eb" />
                </div>
                <div className="resume-file-info">
                  <h4>Uploaded Resume</h4>
                  <p className="filename-text">{user.resumePath}</p>
                  <a
                    href={downloadUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="btn-download"
                  >
                    <Download size={16} /> View / Download Resume
                  </a>
                </div>
              </div>
            ) : (
              <div className="resume-empty">
                <FileText size={48} className="text-muted" />
                <h4>No resume uploaded yet</h4>
                <p>Upload your resume to share with employers when you apply.</p>
              </div>
            )}

            <div className="upload-dropzone mt-4">
              <input
                type="file"
                id="resume-file-input"
                className="file-input-hidden"
                accept=".pdf,.docx"
                onChange={handleFileUpload}
                disabled={uploading}
              />
              <label htmlFor="resume-file-input" className="btn-upload-label">
                <Upload size={18} />
                <span>{uploading ? 'Uploading resume...' : user?.resumePath ? 'Replace Resume (PDF)' : 'Upload Resume (PDF)'}</span>
              </label>
            </div>
          </div>

          <div className="profile-summary-box mt-4">
            <h4>Quick Stats</h4>
            <div className="stats-row">
              <div className="stat-pill">
                <Award size={16} /> {user?.skills?.length || 0} Skills Added
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
